package com.example.animals.di

import com.example.animals.model.AnimalApiService
import dagger.Component

//Component is bridge from module which says what to 'Provide', and the class it's provided to
@Component(modules = [ApiModule::class]) //this component will take functionality from ApiModule and will inject it where we want
interface ApiComponent {
    //made this after module class, **make sure it's an interface

    //here is how we define where we inject the functionality
    fun inject(service: AnimalApiService) //(service: AnimalApiService) important bc tells the system WHERE the functionality will be injected. The 'what' of what will be injected is defined in the @Provides of the Module.
    //can name 'inject' and 'service' whatever you'd like, but animal APIService is important bc tells the system what will be injected
}