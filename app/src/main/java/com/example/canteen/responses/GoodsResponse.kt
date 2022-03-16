package com.example.canteen.responses

import com.example.canteen.models.Goods
import com.google.gson.annotations.SerializedName

data class GoodsResponse(
    @SerializedName("records") var listGoods: List<Goods>,
    @SerializedName("total") val total: Int,
    @SerializedName("size") val pageSize: Int,
    @SerializedName("pages") val pages: Int
)