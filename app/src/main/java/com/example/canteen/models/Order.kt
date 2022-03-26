package com.example.canteen.models

data class Order(
    //订单id
    val id: Int,
    //用户id
    val userid: String,
    //日期
    val cdate: String,
    //订单状态
    val status: Int,
    //详细地址
    val address: String,
    //电话
    val phone: String,
    //快递单号
    val expressid: String,
    //明细详情
    val detailsList: List<OrderDetails>
)