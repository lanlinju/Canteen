package com.example.canteen.models

import android.os.Parcelable
import android.provider.ContactsContract
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") var name: String,
    @SerializedName("password") var password: String ="",
    @SerializedName("image") var image: String = "",
    @SerializedName("email") var email: String ="",
    @SerializedName("nickname") var nickname: String ="",
    @SerializedName("roleName") var roleName: String = "普通员工",
    @SerializedName("phone") var phone: String = ""
): Parcelable
