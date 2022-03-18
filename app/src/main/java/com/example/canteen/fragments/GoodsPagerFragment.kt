package com.example.canteen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.canteen.R
import com.example.canteen.adapters.GoodsAdapter
import com.example.canteen.databinding.FragmentGoodsPagerBinding
import com.example.canteen.listeners.GoodsListener
import com.example.canteen.models.Category
import com.example.canteen.models.Goods
import com.example.canteen.utilities.showDialog
import com.example.canteen.utilities.showLogD
import com.example.canteen.viewmodels.GoodsViewModel


class GoodsPagerFragment(private val category: Category) : Fragment(), GoodsListener{

    private lateinit var binding: FragmentGoodsPagerBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var goodsAdapter: GoodsAdapter
    private var goodsList: MutableList<Goods> = ArrayList()
    private val currentPage = 1
    private val totalAvailablePages = 1
    private var isLoaded = false //避免跳转到其他非viewpager fragment之后返回 会重复加载数据

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_pager, container, false)

        doInitialization()

        return binding.root
    }

    private fun doInitialization() {
       // binding.goodsRecycleView.setHasFixedSize(true)
        goodsViewModel = ViewModelProvider(this)[GoodsViewModel::class.java]
        goodsAdapter = GoodsAdapter(goodsList,this)
        binding.goodsRecycleView.apply {
            adapter = goodsAdapter
            layoutManager =StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }
        if (!isLoaded) {
            getGoodsData()
        }
    }

    private fun getGoodsData() {
        goodsViewModel.getGoodsByCategoryName(categoryName = category.cname)
            .observe(viewLifecycleOwner) {
                isLoaded = true
                if (it.code == 404 || it.code == -1) {
                    requireActivity().showDialog(it.msg) {
                    }
                }
                it?.data.let { goodsResponse ->
                    val oldCount = goodsList.size
                    goodsResponse?.let { it1 -> goodsList.addAll(it1.listGoods) }
                    goodsAdapter.notifyItemRangeInserted(oldCount, goodsList.size)
                }
            }
    }

    override fun onGoodsClicked(goods: Goods) {
        Bundle().apply {
            putParcelable("KEY_GOODS", goods)
            findNavController().navigate(R.id.goodsDetailsFragment,this)
        }

    }

    override fun onStop() {
        super.onStop()
        //goodsList.clear()//跳转到其他非viewpager fragment之后返回 会重复加载数据
        showLogD(category.cname+"GoodsPagerFragment:onStop")
    }

}