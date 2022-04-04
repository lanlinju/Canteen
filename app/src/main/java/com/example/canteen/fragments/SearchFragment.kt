package com.example.canteen.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.adapters.GoodsAdapter
import com.example.canteen.databinding.FragmentSearchBinding
import com.example.canteen.listeners.GoodsListener
import com.example.canteen.models.Goods
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.GoodsViewModel
import java.util.*

class SearchFragment : Fragment(), GoodsListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var goodsAdapter: GoodsAdapter
    private var goodsList: MutableList<Goods> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).binding.smoothBottomBar.notShow()
        goodsViewModel = ViewModelProvider(this)[GoodsViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        doInitialization()
        setObservers()
    }

    private fun setObservers() {
        goodsViewModel.searchResultLiveDate.observe(viewLifecycleOwner) {
            val oldCount: Int = goodsList.size
            goodsList.addAll(it)
            goodsAdapter.notifyItemRangeInserted(oldCount, goodsList.size)
            binding.inputSearch.hideKeyboard()
        }
    }

    private fun doInitialization() {
        binding.imageBack.setOnClickListener { requireActivity().onBackPressed() }
        goodsAdapter = GoodsAdapter(goodsList, this)
        binding.goodsRecyclerView.adapter = goodsAdapter
        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim().isNotEmpty()) {
                    searchGoods(s.toString())
                } else {
                    goodsList.clear()
                    goodsAdapter.notifyDataSetChanged()
                }
            }
        })
        binding.imageSearch.setOnClickListener {
            if (binding.inputSearch.text.toString().trim().isNotBlank()) {
                searchGoods(binding.inputSearch.text.toString().trim())
            } else {
                goodsList.clear()
                goodsAdapter.notifyDataSetChanged()
            }
        }
        binding.inputSearch.showKeyboard()
    }

    private fun searchGoods(query: String) {
        goodsViewModel.searchGoodsByName(query)
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).binding.smoothBottomBar.show()
    }

    override fun onGoodsClicked(goods: Goods) {
        Bundle().apply {
            putParcelable("KEY_GOODS", goods)
            findNavController().navigate(R.id.goodsDetailsFragment, this)
        }
    }

}