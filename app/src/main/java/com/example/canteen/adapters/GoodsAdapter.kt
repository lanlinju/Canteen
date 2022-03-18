package com.example.canteen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen.R
import com.example.canteen.databinding.ItemContainerGoodsBinding
import com.example.canteen.listeners.GoodsListener
import com.example.canteen.models.Goods
import com.example.canteen.utilities.showLog
import com.example.canteen.utilities.showToast

class GoodsAdapter(
    private val goodsList: List<Goods>,
    private val goodsListener: GoodsListener
) :
    RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemContainerGoodsBinding = DataBindingUtil.inflate<ItemContainerGoodsBinding>(
            layoutInflater,
            R.layout.item_container_goods,
            parent,
            false
        )
        return GoodsViewHolder(itemContainerGoodsBinding)
    }

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        holder.bindGoods(goodsList[position])
    }

    override fun getItemCount(): Int {
        return goodsList.size
    }

    inner class GoodsViewHolder(private val binding: ItemContainerGoodsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindGoods(goods: Goods) {
            binding.goods = goods
            binding.imageGoods.setOnClickListener{goodsListener.onGoodsClicked(goods)}
        }
    }

}