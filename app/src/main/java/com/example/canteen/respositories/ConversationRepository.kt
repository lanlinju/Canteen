package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.Conversation
import com.example.canteen.network.ApiClient
import com.example.canteen.network.ConversationApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ConversationRepository {
    private val conversationApiService =
        ApiClient.getRetrofit()?.create(ConversationApiService::class.java)!!
    private val _conversationsLiveDate = MutableLiveData<List<Conversation>>()
    val conversationsLiveData: LiveData<List<Conversation>> get() = _conversationsLiveDate
    private val _conversationIdLiveDate = MutableLiveData<String>()
    val conversationIdLiveDate: LiveData<String> get() = _conversationIdLiveDate

    fun getConversationId(receiverId: String, senderId: String) {
        conversationApiService.getConversationId(receiverId, senderId)
            .enqueue(object : Callback<BaseResponse<String>> {
                override fun onResponse(
                    call: Call<BaseResponse<String>>,
                    response: Response<BaseResponse<String>>
                ) {
                    response.body()?.let {
                        _conversationIdLiveDate.value = it.data
                    }
                }

                override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                    t.message?.showToast()
                }
            })
    }

    fun insertConversion(conversation: Conversation) {
        conversationApiService.insertConversion(conversation)
            .enqueue(object : Callback<BaseResponse<String>> {
                override fun onResponse(
                    call: Call<BaseResponse<String>>,
                    response: Response<BaseResponse<String>>
                ) {
                    _conversationIdLiveDate.value = response.body()?.data
                }

                override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                    t.message?.showToast()
                }
            })
    }

    fun getConversationsByUserId(userId: String) {
        conversationApiService.getConversationsByUserId(userId)
            .enqueue(object : Callback<BaseResponse<List<Conversation>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<Conversation>>>,
                    response: Response<BaseResponse<List<Conversation>>>
                ) {
                    _conversationsLiveDate.value = response.body()?.data
                }

                override fun onFailure(call: Call<BaseResponse<List<Conversation>>>, t: Throwable) {
                    t.message?.showToast()
                }
            })
    }

    fun getAllConversations() {
        conversationApiService.getAllConversations()
            .enqueue(object : Callback<BaseResponse<List<Conversation>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<Conversation>>>,
                    response: Response<BaseResponse<List<Conversation>>>
                ) {
                    _conversationsLiveDate.value = response.body()?.data
                }

                override fun onFailure(call: Call<BaseResponse<List<Conversation>>>, t: Throwable) {
                    t.message?.showToast()
                }
            })
    }

    fun updateLastMessage(conversationId: String, message: String, dateTime: Date) {
        conversationApiService.updateLastMessage(conversationId, message, dateTime)
            .enqueue(object : Callback<BaseResponse<String>> {
                override fun onResponse(
                    call: Call<BaseResponse<String>>,
                    response: Response<BaseResponse<String>>
                ) {
                    //TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                    t.message?.showToast()
                }
            })
    }

    suspend fun deleteConversation(id: String): String? {
        return withContext(Dispatchers.IO) {
            val result = conversationApiService.deleteConversation(id)
            if (result.isSuccessful) {
                result.body()?.data
            } else {
                val errorMessage = "出错了，状态码：${result.code()},信息：${result.message()}"
                throw Exception(errorMessage)
            }
        }
    }
}