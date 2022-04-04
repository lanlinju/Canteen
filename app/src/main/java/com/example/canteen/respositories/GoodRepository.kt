package com.example.canteen.respositories

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Goods
import com.example.canteen.network.ApiClient
import com.example.canteen.network.GoodsApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.GoodsResponse
import com.example.canteen.responses.StatusCode
import com.example.canteen.utilities.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodRepository {

    private var goodsApiService = ApiClient.getRetrofit()?.create(GoodsApiService::class.java)!!
    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> get() = _isDeleted
    private val _searchResultLiveData = MutableLiveData<List<Goods>>()
    val searchResultLiveDate: LiveData<List<Goods>> get() = _searchResultLiveData

    fun searchGoodsByName(name: String) {
        goodsApiService.searchGoodsByName(name)
            .enqueue(object : Callback<BaseResponse<List<Goods>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<Goods>>>,
                    response: Response<BaseResponse<List<Goods>>>
                ) {
                    _searchResultLiveData.value = response.body()?.data
                }

                override fun onFailure(call: Call<BaseResponse<List<Goods>>>, t: Throwable) {
                    t.message?.showToast()
                }
            })
    }

    fun deleteGoods(goods: Goods) {
        goodsApiService.deleteGoods(goods.id).enqueue(object : Callback<BaseResponse<String>> {
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>
            ) {
                _isDeleted.value = true
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                t.message?.showToast()
            }
        })
    }

    fun getGoodsByCategoryName(
        categoryName: String,
        pageNum: Int,
        pageSize: Int
    ): LiveData<BaseResponse<GoodsResponse>> {
        val data: MutableLiveData<BaseResponse<GoodsResponse>> = MutableLiveData()
        goodsApiService.getGooodsByCategoryName(categoryName, pageNum, pageSize)
            .enqueue(object : Callback<BaseResponse<GoodsResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<GoodsResponse>>,
                    response: Response<BaseResponse<GoodsResponse>>
                ) {
                    response.body()?.let {
                        data.value = it
                    }
                }

                override fun onFailure(call: Call<BaseResponse<GoodsResponse>>, t: Throwable) {
                    val baseResponse = BaseResponse<GoodsResponse>(StatusCode.NetWorkError)
                    t.message?.let {
                        baseResponse.msg = it
                        data.value = baseResponse
                    }
                }
            })
        return data
    }
}