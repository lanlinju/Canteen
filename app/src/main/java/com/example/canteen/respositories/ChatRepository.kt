package com.example.canteen.respositories

import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Chat
import com.example.canteen.network.ApiClient
import com.example.canteen.network.ChatApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.utilities.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Path

class ChatRepository {
    private val chatApiService = ApiClient.getRetrofit()?.create(ChatApiService::class.java)
    private val _chatListLiveData: MutableLiveData<List<Chat>> = MutableLiveData()
    val chatListLiveData get() = _chatListLiveData
    fun getMessagesById(receiverId: String, sendId: String) {
        chatApiService?.getMessagesById(receiverId, sendId)
            ?.enqueue(object : Callback<BaseResponse<List<Chat>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<Chat>>>,
                    response: Response<BaseResponse<List<Chat>>>
                ) {
                    _chatListLiveData.value = response.body()?.data
                }

                override fun onFailure(call: Call<BaseResponse<List<Chat>>>, t: Throwable) {
                    t.message.toString()?.showToast()
                }
            })
    }

    fun insertChat(chat: Chat) {
        chatApiService?.insertChat(chat)?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                //  TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.message.toString()?.showToast()
            }
        })
    }
}