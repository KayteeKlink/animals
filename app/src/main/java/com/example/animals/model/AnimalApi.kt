package com.example.animals.model

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalApi {

    //in here defining the methods that we need to call to get the info from api.
    //we define the type of method we use, the info we need to pass to those methods, and the response we will need from those methods

    @GET("getKey") //here pass the endpoint we need to call, aka baseurl.com/getKey -- this corresponds to the method api has defined
    fun getApiKey(): Single<ApiKey> //when we invoke this method, we will get the endpoint getKey through the GET method, no parameters, and the system will return Single of <ApiKey>
    //Single is an observable (just like our LiveData) that can be observed by another entity, and whenever we observe this we will either get a single response or a single error, then finishes.

    @FormUrlEncoded //allows us to use the @Field in our post method
    @POST("getAnimals")
    fun getAnimals(@Field("key") key: String): Single<List<Animal>> //this one needs to pass key parameter to get a response
    //so this method calls endpoint "getAnimals" with a POST method and passes the key as a parameter, then returns a Single List of Animals or an error


}