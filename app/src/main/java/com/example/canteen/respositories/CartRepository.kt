package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Cart
import com.example.canteen.models.Category
import com.example.canteen.network.ApiClient
import com.example.canteen.network.CartApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.GoodsResponse
import com.example.canteen.responses.StatusCode
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import retrofit2.http.Body

class CartRepository {

    private val cartApiService = ApiClient.getRetrofit()?.create(CartApiService::class.java)!!
    private val _cartListLiveData = MutableLiveData<List<Cart>>()
    val cartListLiveData: LiveData<List<Cart>> get() = _cartListLiveData

    fun getAllCarts(userId: String): MutableLiveData<List<Cart>> {
        cartApiService.getAllCarts(userId).enqueue(object : Callback<BaseResponse<List<Cart>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<Cart>>>,
                response: Response<BaseResponse<List<Cart>>>
            ) {
                response.body()?.data.let {
                    _cartListLiveData.value = it
                }

            }

            override fun onFailure(call: Call<BaseResponse<List<Cart>>>, t: Throwable) {
                t.message?.showToast()
            }
        })
        return _cartListLiveData
    }

    suspend fun insertCart(cart: Cart): BaseResponse<String>? {
        return withContext(Dispatchers.IO) {
            val result = cartApiService.insertCart(cart)
            if (result.isSuccessful) {
                result.body()
            } else {
                val errorMessage = "出错了，状态码：${result.code()},信息：${result.message()}"
                throw Exception(errorMessage)
            }
        }
    }
}