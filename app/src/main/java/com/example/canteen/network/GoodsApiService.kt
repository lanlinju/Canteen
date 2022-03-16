package com.example.canteen.network

import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.GoodsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoodsApiService {
    @GET("goods/cname")
    fun getGooodsByCategoryName(
        @Query("name") name: String,
        @Query("pagenum") pagenum: Int = 1,
        @Query("pagesize") pagesize: Int = 10
    ): Call<BaseResponse<GoodsResponse>>
}