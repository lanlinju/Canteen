package com.example.canteen.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Conversation(
    val id: String? = null,
    var lastMessage: String = "",
    val receiverId: String = "",
    val receiverName: String = "",
    val receiverImage: String = "",
    val sendId: String = "",
    val sendName: String = "",
    val sendImage: String = "",
    @SerializedName("timeStamp")
    val dateTime: Date,

    var conversionImage: String = "",
    var conversionName: String = "",
    var conversionId: String = "",
)

