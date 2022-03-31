package com.example.canteen.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") var name: String,
    @SerializedName("password") var password: String,
    @SerializedName("image") var image: String = "",
    @SerializedName("email") val email: String,
    @SerializedName("roleName") var roleName: String = "普通员工",
    @SerializedName("phone") val phone: String = ""
): Parcelable
