package com.example.canteen.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Goods(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") var name: String,
    @SerializedName("thumbnail") var thumbnail: String = "",
    @SerializedName("content") var content: String,
    @SerializedName("price") var price: String = "",
    @SerializedName("cdate") var date: String? = null,
    @SerializedName("categoryId") var categoryId: String = "",
    @SerializedName("dangerNum") var dangerNum: Int = 0,
    @SerializedName("number") var number: String = "",
    @SerializedName("place") var place: String = "",
) : Parcelable
