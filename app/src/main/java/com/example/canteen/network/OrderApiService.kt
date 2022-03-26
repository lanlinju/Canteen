package com.example.canteen.network

import com.example.canteen.models.Order
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OrderApiService {
    @GET("order")
    fun getAllOrderByUserId(@Query("userid") userId:String):Call<BaseResponse<List<Order>>>
}