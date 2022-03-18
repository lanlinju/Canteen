package com.example.canteen.listeners

import com.example.canteen.models.Goods

interface GoodsListener {
    fun onGoodsClicked(goods: Goods)
}