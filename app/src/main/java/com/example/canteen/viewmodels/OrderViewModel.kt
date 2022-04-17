package com.example.canteen.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.canteen.models.Cart
import com.example.canteen.models.Order
import com.example.canteen.responses.BaseResponse
import com.example.canteen.respositories.OrderRepository
import com.example.canteen.utilities.showLog
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel : ViewModel() {

    private val orderRepository = OrderRepository()
    private var _orderListLiveData = MutableLiveData<List<Order>>()
    val orderListLiveData: LiveData<List<Order>> get() = _orderListLiveData
    var isEmptyCarts by mutableStateOf(false)

    fun getAllOrders(userId: String) {
        _orderListLiveData = orderRepository.getAllOrderByUserId(userId)
    }

    fun insertOrder(order: Order) {
        viewModelScope.launch {
            flow {
                try {
                    val info = orderRepository.insertOrder(order)
                    emit(info)
                } catch (e: Exception) {
                    e.message?.showToast()
                }
            }.collect {
                if (it?.code == 0) {
                    isEmptyCarts = true
                    "添加成功".showToast()
                }
            }
        }

    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            flow {
                try {
                    val info = orderRepository.deleteOrder(order.id!!)
                    emit(info)
                } catch (e: Exception) {
                    e.message?.showToast()
                }
            }.collect {
                if (it?.code == 0) {
                    updateUI(order)//删除order
                    "删除成功".showLog()
                } else {
                    "删除失败！".showToast()
                }
            }

        }
    }

    private fun updateUI(order: Order) {//删除order
        val orders = _orderListLiveData.value?.toMutableList()
        orders?.remove(order)
        _orderListLiveData.value = orders!!//更新UI
    }
}

