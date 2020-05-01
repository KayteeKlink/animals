package com.example.animals.di

import com.example.animals.model.AnimalApi
import com.example.animals.model.AnimalApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    //created this first

    private val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net/"

    //how we tell what information we will provide
    @Provides //allows us to be able to inject the return type (AnimalApi) into wherever we want
    fun provideAnimalApi(): AnimalApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //converts to JSON objects. This converts the JSON we retrieve from the backend to the list of the model in our Animal.kt data class
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //converts those JSON objects in observables like defined. This converts what's coming from there^ into Singletons based on the type we declares (Single<Animal> in Animal.kt)
            .build()
            .create(AnimalApi::class.java)

    }

    @Provides
    fun provideAnimalApiService(): AnimalApiService {
        return AnimalApiService()
    }
}