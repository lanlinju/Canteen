package com.example.canteen.fragments.cartfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.compositionLocalOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.canteen.R
import com.example.canteen.databinding.FragmentCartBinding
import com.example.canteen.theme.ui.CanteenM3Theme
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.getPreferenceManager
import com.example.canteen.viewmodels.CartViewModel
import com.example.canteen.viewmodels.OrderViewModel
import com.example.composetutorialsample.ui.theme.CanteenTheme


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        binding = DataBindingUtil.inflate<FragmentCartBinding?>(
            inflater, R.layout.fragment_cart, container, false
        ).apply {
            composeView.setContent {
                CanteenM3Theme {
                    if (orderViewModel.isEmptyCarts){
                        EmptyScreen("当前购物车为空！")
                    }else{
                        CartDetail(cartViewModel,this@CartFragment)
                    }

                }
                cartViewModel.getAllCarts(
                    requireActivity().getPreferenceManager().getString(Constants.KEY_USER_ID)!!
                )
            }
        }
        setListeners()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        cartViewModel.cartListLiveData.observe(viewLifecycleOwner){
            orderViewModel.isEmptyCarts = it.isEmpty()
        }
    }

    private fun setListeners(){
        binding.imageBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}