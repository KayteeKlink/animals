package com.example.animals.di

import android.app.Application
import com.example.animals.util.SharePreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PrefsModule {

    @Provides
    @Singleton //a single instance of a class, bc share prefs helper accesses share prefs which is resource in the android system, that resourcce is the storage of the shared preferences, so we don't want multiple instances accessing resources at the same time, instead a single instance that will access a single resource.
    //^this way objects don't access information before another instance of it changes the info, there's just one instance up to date
    //THIS MEANS OUR COMPONENT ALSO HAS TO BE A SINGLETON (ViewModelComponent)
    fun provideSharedPreferences(app: Application): SharePreferencesHelper {
        return SharePreferencesHelper(app)
    }
}