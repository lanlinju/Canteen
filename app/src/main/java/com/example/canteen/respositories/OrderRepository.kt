package com.example.canteen.respositories

import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Order
import com.example.canteen.network.ApiClient
import com.example.canteen.network.OrderApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun insertOrder(order: Order): BaseResponse<String>? {
        return withContext(Dispatchers.IO) {
            val result = orderApiService.insertOrder(order)
            if (result.isSuccessful) {
                result.body()
            } else {
                val errorMessage = "出错了，状态码：${result.code()},信息：${result.message()}"
                throw Exception(errorMessage)
            }
        }
    }

    suspend fun deleteOrder(orderId: Int): BaseResponse<String>? {
        return withContext(Dispatchers.IO) {
            val result = orderApiService.deleteOrder(orderId)
            if (result.isSuccessful) {
                result.body()
            } else {
                val errorMessage = "出错了，状态码：${result.code()},信息：${result.message()}"
                throw Exception(errorMessage)
            }
        }
    }
}