package me.fridayio.wasapplication.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    fun retroInstance(BASE_URI: String): Retrofit{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URI)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}