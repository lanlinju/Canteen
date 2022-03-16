package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.GoodsResponse
import com.example.canteen.respositories.GoodRepository

class GoodsViewModel() : ViewModel() {

    private val goodRepository = GoodRepository()

    fun getGoodsByCategoryName(categoryName: String, pageNum: Int = 1, pageSize: Int = 8): LiveData<BaseResponse<GoodsResponse>> {
        return goodRepository.getGoodsByCategoryName(categoryName, pageNum, pageSize)
    }
}