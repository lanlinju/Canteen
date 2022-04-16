package com.example.canteen.network

import com.example.canteen.models.Order
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface OrderApiService {
    @GET("order")
    fun getAllOrderByUserId(@Query("userid") userId: String): Call<BaseResponse<List<Order>>>

    @POST("order")
    suspend fun insertOrder(@Body order: Order): Response<BaseResponse<String>>

    @DELETE("order/{id}")
    suspend fun deleteOrder(@Path("id") id: Int): Response<BaseResponse<String>>
}