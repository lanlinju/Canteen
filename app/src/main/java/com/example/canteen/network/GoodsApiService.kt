package com.example.canteen.network

import com.example.canteen.models.Goods
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.GoodsResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GoodsApiService {
    @GET("goods/cname")
    fun getGooodsByCategoryName(
        @Query("name") name: String,
        @Query("pagenum") pagenum: Int = 1,
        @Query("pagesize") pagesize: Int = 10
    ): Call<BaseResponse<GoodsResponse>>

    @GET("goods/{search}")
    fun searchGoodsByName(@Path("search") search: String):Call<BaseResponse<List<Goods>>>

    @POST("goods")
    suspend fun insertGoods(@Body goods: Goods):Response<BaseResponse<String>>

//    @HTTP(method = "DELETE",path = "goods",hasBody = true)
    @DELETE("goods/{id}")//@delete不能传递Body
    fun deleteGoods(@Path("id") id: String):Call<BaseResponse<String>>

    @PUT("goods")
    suspend fun updateGoods(@Body goods: Goods):Response<BaseResponse<String>>

}