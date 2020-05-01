package com.example.animals.model

import com.example.animals.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class AnimalApiService {

    @Inject
    lateinit var api: AnimalApi

    init {
        //generated class "Dagger{{OurComponentInterfaceName}}"
        DaggerApiComponent.create().inject(this) //injecting (fun inject(service: AnimalApiService) [defined in ApiComponent] into 'this' [AnimalApiService]
    }

    fun getApiKey(): Single<ApiKey> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimals(key)
    }
}