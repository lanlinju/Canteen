package com.example.canteen.models

data class Order(
    //订单id
    val id: Int? = null,
    //用户id
    val userid: String? = null,
    //日期
    val cdate: String? = null,
    //订单状态
    val status: Int? = null,
    //详细地址
    val address: String,
    //电话
    val phone: String,
    //快递单号
    val expressid: String? = null,
    //明细详情
    val detailsList: List<OrderDetails>? = null
)