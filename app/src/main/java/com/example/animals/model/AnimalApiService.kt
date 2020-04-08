package com.example.animals.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AnimalApiService {

    private val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) //converts to JSON objects. This converts the JSON we retrieve from the backend to the list of the model in our Animal.kt data class
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //converts those JSON objects in observables like defined. This converts what's coming from there^ into Singletons based on the type we declares (Single<Animal> in Animal.kt)
        .build()
        .create(AnimalApi::class.java)

    fun getApiKey(): Single<ApiKey> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimals(key)
    }
}