package com.example.canteen.network

import com.example.canteen.models.User
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiService {
    @GET("user/login")
    fun signIn(@Query("email") email: String, @Query("password") password: String): Call<BaseResponse<User>>

    @POST("user/register")
    fun signUp(@Body user: User): Call<BaseResponse<String>>

    @GET("user/ltest")
    fun test(@Query("id")  id:String): Call<BaseResponse<User>>
}