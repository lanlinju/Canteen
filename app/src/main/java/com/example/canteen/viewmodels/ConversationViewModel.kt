package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.Conversation
import com.example.canteen.respositories.ConversationRepository
import java.util.*

class ConversationViewModel : ViewModel() {
    private val conversationRepository = ConversationRepository()
    val conversationsLiveData: LiveData<List<Conversation>> get() = conversationRepository.conversationsLiveData
    val conversationIdLive get() = conversationRepository.conversationIdLiveDate

    fun getConversationId(receiverId: String, senderId: String) {
        conversationRepository.getConversationId(receiverId, senderId)
    }

    fun insertConversion(conversation: Conversation) {
        conversationRepository.insertConversion(conversation)
    }

    fun getAllConversations() {
        conversationRepository.getAllConversations()
    }

    fun getConversationsByUserId(userId:String) {
        conversationRepository.getConversationsByUserId(userId)
    }

    fun updateLastMessage(conversationId: String, message: String,dateTime: Date) {
        conversationRepository.updateLastMessage(conversationId, message,dateTime)
    }
}