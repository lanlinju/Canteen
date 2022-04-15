package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canteen.models.Cart
import com.example.canteen.responses.BaseResponse
import com.example.canteen.respositories.CartRepository
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val cartRepository = CartRepository()
    private var _cartListLiveData = MutableLiveData<List<Cart>>()
    val cartListLiveData: LiveData<List<Cart>> get() = cartRepository.cartListLiveData

    fun getAllCarts(userId: String) {
        _cartListLiveData = cartRepository.getAllCarts(userId)
    }

    fun insertCart(cart: Cart) {
        viewModelScope.launch {
            flow {
                try {
                    val info = cartRepository.insertCart(cart)
                    emit(info!!)
                } catch (e: Exception) {
                    e.message?.showToast()
                }
            }.collect {
                if (it.code == 0) {
                    "添加成功".showToast()
                }
            }
        }

    }
}