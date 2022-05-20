package com.fahruaz.storyapp.api

import com.fahruaz.storyapp.Response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field(value = "name") name: String,
        @Field(value = "email") email: String,
        @Field(value = "password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field(value = "email") email: String,
        @Field(value = "password") password: String
    ): Call<LoginResponse>

    @GET("/v1/stories")
    fun getAllStories(
        @Header("Authorization") authHeader: String,
        @Query("location")value: String
    ): Call<GetStoryResponse>

    @GET("/v1/stories")
    suspend fun getStoryPaging(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetStoryResponse

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") authHeader: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double
    ): Call<AddStoryResponse>

    @Multipart
    @POST("stories")
    fun addStoryWithoutLocation(
        @Header("Authorization") authHeader: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoryResponse>

}