package me.fridayio.wasapplication.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("ml/")
    fun postML(@Body req: PostRequest) : Call<PostResponse>
}