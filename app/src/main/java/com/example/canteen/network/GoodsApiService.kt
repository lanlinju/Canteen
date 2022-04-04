package com.example.canteen.network

import com.example.canteen.models.Goods
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.GoodsResponse
import retrofit2.Call
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

//    @HTTP(method = "DELETE",path = "goods",hasBody = true)
    @DELETE("goods/{id}")
    fun deleteGoods(@Path("id") id: String):Call<BaseResponse<String>>
}