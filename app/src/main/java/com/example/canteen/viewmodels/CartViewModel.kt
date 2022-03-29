package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canteen.models.Cart
import com.example.canteen.respositories.CartRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val cartRepository = CartRepository()
    private var _cartListLiveData = MutableLiveData<List<Cart>>()
    val cartListLiveData: LiveData<List<Cart>> get() = cartRepository.cartListLiveData

    fun getAllCarts(userId: String) {
        _cartListLiveData = cartRepository.getAllCarts(userId)
    }
}