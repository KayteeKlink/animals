package com.example.animals.di

import com.example.animals.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton //component has to be a singleton bc PrefsModule provides a singleton
@Component(modules = [ApiModule::class, PrefsModule::class, AppModule::class])
interface ViewModelComponent {

    fun inject(viewModel: ListViewModel)

}