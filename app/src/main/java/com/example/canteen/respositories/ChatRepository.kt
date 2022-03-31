package com.example.canteen.respositories

import com.example.canteen.models.Chat
import com.example.canteen.network.ApiClient
import com.example.canteen.network.ChatApiService
import com.example.canteen.utilities.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository {
    private val chatApiService = ApiClient.getRetrofit()?.create(ChatApiService::class.java)!!

    fun insertChat(chat: Chat){
        chatApiService.insertChat(chat).enqueue(object : Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                //TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
               // TODO("Not yet implemented")
                t.message?.showToast()
            }
        })
    }
}