package com.example.canteen.network

import com.example.canteen.models.Category
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.http.GET

interface CategoryApiService {
    @GET("category")
    fun getAllCategory(): Call<BaseResponse<List<Category>>>
}