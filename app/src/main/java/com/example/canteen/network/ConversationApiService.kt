package com.example.canteen.network

import com.example.canteen.models.Conversation
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface ConversationApiService {
    @GET("conversation")
    fun getAllConversations(): Call<BaseResponse<List<Conversation>>>

    @PUT("conversation/{conversationId}/{message}")
    fun updateLastMessage(
        @Path("conversationId") conversationId: String,
        @Path("message") message: String
    ): Call<BaseResponse<String>>
}