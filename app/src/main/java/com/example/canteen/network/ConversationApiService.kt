package com.example.canteen.network

import com.example.canteen.models.Conversation
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface ConversationApiService {
    @GET("conversation")
    fun getAllConversations(): Call<BaseResponse<List<Conversation>>>

    @GET("conversation/{userId}")
    fun getConversationsByUserId(@Path("userId") userId: String): Call<BaseResponse<List<Conversation>>>

    @PUT("conversation/{conversationId}/{message}")
    fun updateLastMessage(
        @Path("conversationId") conversationId: String,
        @Path("message") message: String,
        @Body dateTime: Date
    ): Call<BaseResponse<String>>

    @POST("conversation")
    fun insertConversion(@Body conversation: Conversation): Call<BaseResponse<String>>

    @GET("conversation/{receiverId}/{sendId}")
    fun getConversationId(
        @Path("receiverId") receiverId: String,
        @Path("sendId") sendId: String
    ): Call<BaseResponse<String>>

    @DELETE("conversation/{id}")
    suspend fun deleteConversation(@Path("id") id: String): Response<BaseResponse<String>>
}