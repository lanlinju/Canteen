package com.example.canteen.network

import com.example.canteen.models.Cart
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface CartApiService {
    @GET("cart/{userid}")
    fun getAllCarts(@Path("userid") userId: String): Call<BaseResponse<List<Cart>>>

    @POST("cart")
    suspend fun insertCart(@Body cart: Cart): Response<BaseResponse<String>>

    @PUT("cart")
    suspend fun updateCart(@Body cart: Cart): Response<BaseResponse<String>>

    @DELETE("cart/{userid}/{goodsid}")
    suspend fun deleteCart(
        @Path("userid") userId: String,
        @Path("goodsid") goodsId: String
    ): Response<BaseResponse<String>>
}