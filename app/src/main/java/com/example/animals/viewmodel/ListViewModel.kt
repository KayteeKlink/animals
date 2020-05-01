package com.example.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.animals.di.AppModule
import com.example.animals.di.DaggerViewModelComponent
import com.example.animals.model.Animal
import com.example.animals.model.AnimalApiService
import com.example.animals.model.ApiKey
import com.example.animals.util.SharePreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

//this will expose the variables, instantiate the var's with some values, and allow the view to connect with them
class ListViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var apiService: AnimalApiService

    @Inject
    lateinit var prefs: SharePreferencesHelper


    init {
        //cannot simplu .create() anymore because AppModule takes a parameter of Application unlike the other modules, so need to build it with the application passed.
        DaggerViewModelComponent.builder().appModule(AppModule(getApplication()))
            .build()
            .inject(this)

    }

    /* Why use AndroidViewModel and Applications in our ViewModel, why not extend the ViewModel class which doesn't require 'Application'?
        Bc of how our backend works, our api will require a key, and we will need to store that key in our Shared Preference, in our local app.
        Shared Preference always requires a context, which can either be an Application or an Activity.
        In our case we're using Application. Google recommends that you don't use an Activity context inside your view model, bc Activity context can be destroyed and recreated, and we don't want to store a reference that can be destroyed/recreated in our ViewModel. Defeats the purpose of separation of ViewModels
        AndroidViewModel is a class provided by Google to solve this problem, when you need a context in our VM but don't want to use an Activity context^, so we store an Application context which is not part of our view (it's the whole app) and the application context has a lifetime of the application, so it lives as long as the application lives, as opposed to as long the activity lives.
    */


    // lazy means the system is not going to instantiate this live data variable unless when it is needed. If we never use it in our code, then it's not created. Helps to make our program efficient
    // MutableLiveData: LiveData is observable that provides diff values for whoever is listening to that observable. Mutable just means that we can change the values and add new values to that list
    //below, it provides a list of variables

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() } //going to be instantiated when we have a problem and don't receive any info from the api, or we get an error from api
    val loading by lazy { MutableLiveData<Boolean>() }  //loading is going to provide info that the system is processing in background and trying to get info. So loading var is set to true, we'll show a loading spinner

    private val disposable = CompositeDisposable()
    //a 'disposable 'is an rxJava construct that takes the result of an observable and is able to dismiss it when the lifecycle of the view model is finished


    private var invalidApiKey = false

    fun refresh() {
        //will start the retreival of data from back end, eventually
        loading.value = true //starts the loading

        invalidApiKey = false

        val key = prefs.getApiKey()

        //where we check if we already have a key or not
        if (key.isNullOrEmpty()) {
            getKey()
        } else {
            getAnimals(key)
        }


    }

    fun hardRefresh() { //will not check if there's a key in the preferences, call this method in out list fragment
        loading.value = true
        getKey()
    }

    private fun getKey() {
        disposable.add(
            apiService.getApiKey() //this will actually call the backend api and try to communicate with the backend service. We don't know how long this will take, so we don't want to do this on the MainThread application cause it will block the user's view.
                //Android System actually doesn't allow you to do this^ on the backend thread, need the below so can do on a background thread
                .subscribeOn(Schedulers.newThread()) //so this is saying we are going to do the getApiKey on a background thread. Hence creates that background thread
                .observeOn(AndroidSchedulers.mainThread()) //then the operation finishes, we don't want to return the restult on the bg thread, we want the result on the main thread so we can show that result on that view
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(key: ApiKey) { //if get reply from backend
                        if (key.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            //adding here to save the key to shared preferences
                            prefs.saveApiKey(key.key)
                            getAnimals(key.key)
                        }
                    }

                    override fun onError(e: Throwable) { //if get error from backend
                        e.printStackTrace() //log the error
                        loading.value = false
                        loadError.value = true
                    }
                })  //then we subscribe (?)
        )
    }

    private fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()) //then the operation finishes, we don't want to return the restult on the bg thread, we want the result on the main thread so we can show that result on that view
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(list: List<Animal>) {
                        loadError.value = false
                        animals.value = list
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (!invalidApiKey) { // If invalid api key is false, which it's set to default false, so this will always be true when going to an error
                            invalidApiKey = true // then we'll set the api kei to true
                            getKey() //and go to getKey, which will cell get animals again, then when we get ? idk dude **look into this + context
                        } else {
                            e.printStackTrace()
                            loading.value = false
                            animals.value = null
                            loadError.value = true
                        }
                    }
                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        //when we attach to Single observables, and the ListViewModel is destroyed, then we will still stay attached by a link to the service
        //so disposable tracks all those links, and here clears them at the end so we don't get a memory leak
    }
}