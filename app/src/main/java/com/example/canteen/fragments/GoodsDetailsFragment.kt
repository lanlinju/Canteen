package com.example.canteen.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.unit.Constraints
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.databinding.FragmentGoodsDetailsBinding
import com.example.canteen.models.Goods
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.GoodsViewModel
import java.util.*


class GoodsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentGoodsDetailsBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var goodsDetails: Goods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).binding.smoothBottomBar.notShow()
        goodsViewModel = ViewModelProvider(this)[GoodsViewModel::class.java]
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_goods_details, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        goodsDetails = arguments?.getParcelable<Goods>("KEY_GOODS")!!
        binding.goods = goodsDetails
        requireActivity().apply {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            //设置专栏栏和导航栏的底色，透明
            window.statusBarColor = Color.TRANSPARENT
        }
        setListeners()
        setObservers()

    }

    private fun setObservers() {
        goodsViewModel.isDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                getString(R.string.success_delete).showToast()
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            buttonDelete.setOnClickListener {
                if (requireContext().getPreferenceManager().getString(Constants.KEY_ROSE_NAME)!! == Constants.KEY_ROSE_SYSTEM){
                    goodsViewModel.deleteGoods(goodsDetails)
                } else {
                    getString(R.string.less_authority).showToast()
                }
            }
            imageBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            textReadMore.setOnClickListener {
                if (textReadMore.text.toString() == getString(R.string.read_more)) {
                    textDescription.maxLines = Int.MAX_VALUE
                    textDescription.ellipsize = null
                    textReadMore.setText(R.string.read_less)
                } else {
                    textDescription.maxLines = 4
                    textDescription.ellipsize = TextUtils.TruncateAt.END
                    textReadMore.setText(R.string.read_more)
                }
            }
        }
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