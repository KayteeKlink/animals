package com.example.animals

import android.app.Application
import com.example.animals.di.PrefsModule
import com.example.animals.util.SharePreferencesHelper

class PrefsModuleTest(val mockPrefs: SharePreferencesHelper): PrefsModule() {
    override fun provideSharedPreferences(app: Application): SharePreferencesHelper {
        return mockPrefs
    }
}