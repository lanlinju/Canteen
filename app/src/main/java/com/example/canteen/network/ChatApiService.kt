package com.example.canteen.network


import com.example.canteen.models.Chat
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApiService {
    @POST("chat")
    fun insertChat(@Body chat: Chat): Call<String>

    @GET("chat/{receiverId}/{sendId}")
    fun getMessagesById(
        @Path("receiverId") receiverId: String,
        @Path("sendId") sendId: String
    ):Call<BaseResponse<List<Chat>>>
}