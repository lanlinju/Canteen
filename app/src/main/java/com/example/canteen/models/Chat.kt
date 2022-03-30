package com.example.canteen.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Chat (
    val id: String? = null,
    val receiverId: String,
    val sendId: String,
    val message: String,
    val conversationId: String,
    @SerializedName("timeStamp")
    val dateTime: String,
    val dateObject: Date
)