package com.example.canteen.respositories

import android.telecom.ConnectionService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Conversation
import com.example.canteen.network.ApiClient
import com.example.canteen.network.ConversationApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.utilities.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Path

class ConversationRepository {
    private val conversationApiService =
        ApiClient.getRetrofit()?.create(ConversationApiService::class.java)!!
    private val _conversationLiveDate = MutableLiveData<List<Conversation>>()
    val conversationLiveData: LiveData<List<Conversation>> get() = _conversationLiveDate

    fun getAllConversations() {
        conversationApiService.getAllConversations()
            .enqueue(object : Callback<BaseResponse<List<Conversation>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<Conversation>>>,
                    response: Response<BaseResponse<List<Conversation>>>
                ) {
                    _conversationLiveDate.value = response.body()?.data
                }

                override fun onFailure(call: Call<BaseResponse<List<Conversation>>>, t: Throwable) {
                    t.message?.showToast()
                }
            })
    }

    fun updateLastMessage(conversationId: String, message: String) {
        conversationApiService.updateLastMessage(conversationId, message)
            .enqueue(object : Callback<BaseResponse<String>> {
                override fun onResponse(
                    call: Call<BaseResponse<String>>,
                    response: Response<BaseResponse<String>>
                ) {
                    //TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                    //TODO("Not yet implemented")
                    t.message?.showToast()
                }
            })
    }
}