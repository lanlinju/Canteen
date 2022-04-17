package com.example.canteen.fragments.orderfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.canteen.R
import com.example.canteen.components.EmptyScreen
import com.example.canteen.databinding.FragmentOrdersBinding
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.getPreferenceManager
import com.example.canteen.utilities.showLog
import com.example.canteen.viewmodels.OrderViewModel
import com.example.composetutorialsample.ui.theme.CanteenTheme


class OrdersFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var binding: FragmentOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        binding = DataBindingUtil.inflate<FragmentOrdersBinding?>(
            inflater,
            R.layout.fragment_orders,
            container,
            false
        ).apply {

            composeView.setContent {
                val orderList by orderViewModel.orderListLiveData.observeAsState()
                CanteenTheme() {
                    if (orderList?.size == 0){
                        EmptyScreen(text = "当前订单为空！")
                    }else{
                        OrderDetail(orderViewModel = orderViewModel, orderList = orderList)
                    }

                }
            }

            requireActivity().getPreferenceManager().getString(
                Constants.KEY_USER_ID
            )?.let { userId ->
                orderViewModel.getAllOrders(userId = userId)
            }
        }
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}