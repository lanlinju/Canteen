package com.example.canteen.network

import com.example.canteen.models.User
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.http.*

interface UserApiService {
    @GET("user/login")
    fun signIn(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<BaseResponse<User>>

    @POST("user/register")
    fun signUp(@Body user: User): Call<BaseResponse<String>>

    @GET("user/{id}")
    fun getUerById(@Path("id") id: String): Call<BaseResponse<User>>

    @GET("user/all")
    fun getAllUsers(): Call<BaseResponse<List<User>>>

    @GET("user/userid")
    fun test(@Query("id") id: String): Call<BaseResponse<User>>
}