package com.example.canteen.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.Goods
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.GoodsResponse
import com.example.canteen.respositories.GoodRepository
import com.example.canteen.utilities.showToast

class GoodsViewModel() : ViewModel() {

    private val goodsRepository = GoodRepository()
    private val _progressBarVisibility = MutableLiveData(View.GONE)
    val progressBarVisibility :LiveData<Int> get() = _progressBarVisibility
    val searchResultLiveDate : LiveData<List<Goods>> get() = goodsRepository.searchResultLiveDate
    val isDeleted:LiveData<Boolean> get() = goodsRepository.isDeleted

    fun searchGoodsByName(name: String){
        goodsRepository.searchGoodsByName(name)
    }

    fun deleteGoods(goods: Goods){
        goodsRepository.deleteGoods(goods)
    }

    fun getGoodsByCategoryName(categoryName: String, pageNum: Int = 1, pageSize: Int = 8): LiveData<BaseResponse<GoodsResponse>> {
        return goodsRepository.getGoodsByCategoryName(categoryName, pageNum, pageSize)
    }

    fun toggleProgressBarVisibility() {
        if (_progressBarVisibility.value == View.GONE) {
            _progressBarVisibility.value = View.VISIBLE
        } else {
            _progressBarVisibility.value = View.GONE
        }
    }
}