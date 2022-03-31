package com.example.canteen.network


import com.example.canteen.models.Chat
import retrofit2.Call
import retrofit2.http.POST

interface ChatApiService {
    @POST("chat")
    fun insertChat(chat: Chat): Call<Int>
}