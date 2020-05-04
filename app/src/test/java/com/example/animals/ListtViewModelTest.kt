package com.example.animals

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.animals.di.AppModule
import com.example.animals.di.DaggerViewModelComponent
import com.example.animals.model.AnimalApiService
import com.example.animals.util.SharePreferencesHelper
import com.example.animals.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Rule
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

    var listViewModel = ListViewModel(application)

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
}