package com.example.canteen.network

import com.example.canteen.models.Conversation
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.http.*


interface ConversationApiService {
    @GET("conversation")
    fun getAllConversations(): Call<BaseResponse<List<Conversation>>>

    @PUT("conversation/{conversationId}/{message}")
    fun updateLastMessage(
        @Path("conversationId") conversationId: String,
        @Path("message") message: String
    ): Call<BaseResponse<String>>

    @POST("conversation")
    fun insertConversion(@Body conversation: Conversation): Call<BaseResponse<String>>

    @GET("conversation/{receiverId}/{sendId}")
    fun getConversationId(
        @Path("receiverId") receiverId: String,
        @Path("sendId") sendId: String
    ): Call<BaseResponse<String>>
}