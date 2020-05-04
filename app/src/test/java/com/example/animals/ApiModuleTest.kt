package com.example.animals

import com.example.animals.di.ApiModule
import com.example.animals.model.AnimalApiService

class ApiModuleTest(val mockAnimalAPiSerive: AnimalApiService) :
    ApiModule() {

    override fun provideAnimalApiService(): AnimalApiService {
        return mockAnimalAPiSerive
    }
}