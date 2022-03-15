package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.Category
import com.example.canteen.responses.BaseResponse
import com.example.canteen.respositories.CategoryRepository

class HomeViewModel : ViewModel(){
    private val categoryRepository = CategoryRepository()
    private var mCategoryLiveData = MutableLiveData<BaseResponse<List<Category>>>()
    val categoryLiveData: LiveData<BaseResponse<List<Category>>> get() = getAllCategory()

    private fun getAllCategory():LiveData<BaseResponse<List<Category>>>{
        return categoryRepository.getAllCategory()
    }

}