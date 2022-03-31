package com.example.canteen.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Chat (
    val id: String? = null,
    val receiverId: String,
    @SerializedName("sendId")
    val senderId: String,
    val message: String,
    @SerializedName("timeStamp")
    val dateTime: Date,
)