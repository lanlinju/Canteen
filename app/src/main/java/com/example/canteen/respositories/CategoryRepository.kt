package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Category
import com.example.canteen.models.User
import com.example.canteen.network.ApiClient
import com.example.canteen.network.CategoryApiService
import com.example.canteen.network.UserApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.StatusCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRepository {

    private var categoryApiService: CategoryApiService = ApiClient.getRetrofit()?.create(
        CategoryApiService::class.java
    )!!

    fun getAllCategory(): LiveData<BaseResponse<List<Category>>> {
        val data: MutableLiveData<BaseResponse<List<Category>>> = MutableLiveData()
        categoryApiService.getAllCategory().enqueue(object : Callback<BaseResponse<List<Category>>>{
            override fun onResponse(
                call: Call<BaseResponse<List<Category>>>,
                response: Response<BaseResponse<List<Category>>>
            ) {
                data.value = response?.body()
            }

            override fun onFailure(call: Call<BaseResponse<List<Category>>>, t: Throwable) {
                val baseResponse = BaseResponse<List<Category>>(StatusCode.NetWorkError)
                t.message?.let {
                    baseResponse.msg = it
                    data.value = baseResponse
                }
            }
        })
        return data
    }
}