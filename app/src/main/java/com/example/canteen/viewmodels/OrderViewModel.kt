package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.Order
import com.example.canteen.respositories.OrderRepository

class OrderViewModel : ViewModel() {

    private val orderRepository = OrderRepository()
    private var _orderListLiveData = MutableLiveData<List<Order>>()
    val orderListLiveData: LiveData<List<Order>> get() = _orderListLiveData

    fun getAllOrders(userId: String) {
        _orderListLiveData = orderRepository.getAllOrderByUserId(userId)
    }
}