package com.example.canteen.models

import com.google.gson.annotations.SerializedName


data class Cart(
    @SerializedName("userid") val userId: String,
    @SerializedName("goodsid") val goodsId: String,
    @SerializedName("num") var num: Int,
    @SerializedName("goods") val goods: Goods? = null
)