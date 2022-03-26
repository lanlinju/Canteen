package com.example.canteen.respositories

import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Order
import com.example.canteen.network.ApiClient
import com.example.canteen.network.OrderApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.utilities.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository {

    private val orderApiService = ApiClient.getRetrofit()?.create(OrderApiService::class.java)!!

    fun getAllOrderByUserId(userId: String): MutableLiveData<List<Order>> {
        val data = MutableLiveData<List<Order>>()
        orderApiService.getAllOrderByUserId(userId = userId)
            .enqueue(object : Callback<BaseResponse<List<Order>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<Order>>>,
                    response: Response<BaseResponse<List<Order>>>
                ) {
                    data.value = response.body()?.data
                }
                override fun onFailure(call: Call<BaseResponse<List<Order>>>, t: Throwable) {
                    t.message?.showToast()
                }
            })
        return data
    }
}