package com.example.canteen.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



@Parcelize
data class Goods(
    @SerializedName("id") val id: String,
    @SerializedName("name") var name: String,
    @SerializedName("thumbnail") var thumbnail: String,
    @SerializedName("content") var content: String,
    @SerializedName("price") var price: String,
    @SerializedName("cdate") var date: String,
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("dangerNum") var dangerNum: String,
    @SerializedName("number") var number: String,
    @SerializedName("place") var place: String,
    @SerializedName("providerId") val providerId: String,
) : Parcelable
