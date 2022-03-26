package com.example.canteen.models

data class OrderDetails(
    //订单id
    var orderid: Int,
    //商品id
    val goodsid: String,
    //数量
    val num: Int,
    //价格
    val price: Float,
    //商品信息
    val goods: Goods
)
