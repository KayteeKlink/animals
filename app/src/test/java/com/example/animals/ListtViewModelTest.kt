package com.example.animals

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.animals.di.AppModule
import com.example.animals.di.DaggerViewModelComponent
import com.example.animals.model.Animal
import com.example.animals.model.AnimalApiService
import com.example.animals.model.ApiKey
import com.example.animals.util.SharePreferencesHelper
import com.example.animals.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class ListtViewModelTest {
    @get:Rule
    var rule =
        InstantTaskExecutorRule() //allows us to execute a task instantly and receive the response

    @Mock
    lateinit var animalService: AnimalApiService

    @Mock
    lateinit var prefs: SharePreferencesHelper

    val application =
        Mockito.mock(Application::class.java) //app is not created as lateinit var bc we need it right away to instantiate the ListViewModel class below

    var listViewModel = ListViewModel(
        application,
        true
    ) //this true will tell the listViewModel to go through the test constructor

    private val key = "Test key"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
            .appModule(AppModule(application)) //providing the mock application here
            .apiModule(ApiModuleTest(animalService))
            .prefsModule(PrefsModuleTest(prefs))
            .build()
            .inject(listViewModel)
        //^ want to inject this mock component
    }

    @Before
    //RxJava setup
    fun setupRxSchedulers() {
        //this will set up our new thread and main thread before any tests
        val immediate =
            object : Scheduler() {  //Alt + Enter on this `object` to see override methods!
                override fun createWorker(): Worker {
                    return ExecutorScheduler.ExecutorWorker(
                        Executor { it.run() },
                        true
                    ) //so it runs instantly whenever we get an executor
                }

            }

        //create newThread() and mainThread() in ListViewModel and have it return immediately here for our tests
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

    @Test
    fun getAnimalSuccess() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        //if we get an api call then we return a key (when we already have a key in our system?)
        val animal = Animal("cow", null, null, null, null, null, null)
        val animalList =
            listOf(animal) //gives us the list of animals we want to return when the viewmodel does a call to getAnimals

        val testSingle = Single.just(animalList)

        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(
            false,
            listViewModel.loadError.value
        ) //checks that we're not getting any errors
        Assert.assertEquals(
            false,
            listViewModel.loading.value
        ) //assert that loading boolean has false value also
    }

    @Test
    fun getAnimalError() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)

        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("OK", key))

        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)

        listViewModel.refresh()

        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
    }

    @Test
    fun getKeySuccess() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(null)

        val apiKey = ApiKey("OK", key)

        val keySingle = Single.just(apiKey)

        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)

        val animal = Animal("cow", null, null, null, null, null, null)
        val animalsList = listOf(animal)

        val testSingle = Single.just(animalsList)

        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(1, listViewModel.animals.value?.size)
        //cannot get to get animals with out the success of geKey^
    }

    @Test
    fun getKeyFailure() {
        val keySingle = Single.error<ApiKey>(Throwable())

        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)

        listViewModel.hardRefresh()

        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
        Assert.assertEquals(null, listViewModel.animals.value)
    }
}