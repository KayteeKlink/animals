package com.example.animals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executor

class ListtViewModelTest {
    @get:Rule
    var rule =
        InstantTaskExecutorRule() //allows us to execute a task instantly and receive the response

    @Before
    fun setupRxSchedulers() {
        //this will set up our new thread and main thread before any tests
        val immediate =
            object : Scheduler() {  //Alt + Enter on this `object` to see override methods!
                override fun createWorker(): Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true) //so it runs instantly whenever we get an executor
                }

            }

        //create newThread() and mainThread() in ListViewModel and have it return immediately here for our tests
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{ scheduler -> immediate }
    }
}