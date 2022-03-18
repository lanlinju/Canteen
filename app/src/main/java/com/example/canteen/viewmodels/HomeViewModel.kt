package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.Category
import com.example.canteen.responses.BaseResponse
import com.example.canteen.respositories.CategoryRepository

class HomeViewModel : ViewModel() {

    private val categoryRepository = CategoryRepository()
    var isLoaded = false //避免跳转到其他fragment之后返回 会重复加载数据
    var categoryList: List<Category> = ArrayList()
    // private var mCategoryLiveData = MutableLiveData<BaseResponse<List<Category>>>()

//    val categoryLiveData: LiveData<BaseResponse<List<Category>>> = mCategoryLiveData

    fun getAllCategory(): LiveData<BaseResponse<List<Category>>> {
        return categoryRepository.getAllCategory()
    }

}