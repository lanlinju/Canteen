package com.example.canteen.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.databinding.FragmentGoodsDetailsBinding
import com.example.canteen.models.Goods
import com.example.canteen.utilities.notShow
import com.example.canteen.utilities.show
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class GoodsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentGoodsDetailsBinding
    private lateinit var goodsDetails: Goods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).binding.smoothBottomBar.notShow()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_goods_details, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        goodsDetails = arguments?.getParcelable<Goods>("KEY_GOODS")!!
        requireActivity().apply {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            //设置专栏栏和导航栏的底色，透明
            window.statusBarColor = Color.TRANSPARENT
        }

        binding.goods = goodsDetails

    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).apply {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            //设置专栏栏和导航栏的底色，透明
            window.statusBarColor = Color.parseColor("#77A7EF")
            binding.smoothBottomBar.show()
        }
    }
}