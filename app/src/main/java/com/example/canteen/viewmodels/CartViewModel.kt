package com.example.canteen.viewmodels

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.*
import com.example.canteen.models.Cart
import com.example.canteen.responses.BaseResponse
import com.example.canteen.respositories.CartRepository
import com.example.canteen.utilities.showLog
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class CartViewModel : ViewModel() {

    private val cartRepository = CartRepository()
    private var _cartListLiveData: MutableLiveData<List<Cart>> = MutableLiveData()
    val cartListLiveData: LiveData<List<Cart>> get() = cartRepository.cartListLiveData

    fun getAllCarts(userId: String) {
        _cartListLiveData = cartRepository.getAllCarts(userId)
    }

    fun deleteCart(cart: Cart) {
        viewModelScope.launch {
            flow {
                try {
                    val info = cartRepository.deleteCart(cart.userId, cart.goodsId)
                    emit(info)
                } catch (e: Exception) {
                    e.message?.showToast()
                }

            }.collect {
                if (it?.code == 0) {
                    removeCart(cart)
                    "删除成功".showLog()
                } else {
                    "删除失败！".showToast()
                }
            }
        }
    }

    private fun removeCart(cart: Cart) {
        val carts = _cartListLiveData.value?.toMutableList()
        carts?.remove(cart)
        _cartListLiveData.value = carts!!//更新UI
    }

    fun insertCart(cart: Cart) {
        viewModelScope.launch {
            flow {
                try {
                    val info = cartRepository.insertCart(cart)
                    emit(info)
                } catch (e: Exception) {
                    e.message?.showToast()
                }
            }.collect {
                if (it?.code == 0) {
                    "添加成功".showToast()
                }
            }
        }
    }

    fun updateCart(cart: Cart) {
        viewModelScope.launch {
            flow {
                try {
                    val info = cartRepository.updateCart(cart)
                    emit(info)
                } catch (e: Exception) {
                    e.message?.showToast()
                }
            }.collect {
                if (it?.code == 0) {
                    "更新成功".showLog()
                }
            }
        }
    }
}