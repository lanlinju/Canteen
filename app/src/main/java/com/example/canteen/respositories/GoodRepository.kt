package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Category
import com.example.canteen.models.User
import com.example.canteen.network.ApiClient
import com.example.canteen.network.CategoryApiService
import com.example.canteen.network.GoodsApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.GoodsResponse
import com.example.canteen.responses.StatusCode
import com.example.canteen.utilities.showLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodRepository {

    private var goodsApiService = ApiClient.getRetrofit()?.create(GoodsApiService::class.java)!!

    fun getGoodsByCategoryName(categoryName: String, pageNum: Int, pageSize: Int): LiveData<BaseResponse<GoodsResponse>> {
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