package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.Conversation
import com.example.canteen.respositories.ConversationRepository

class ConversationViewModel : ViewModel() {
    private val conversationRepository = ConversationRepository()
    val conversationLiveData: LiveData<List<Conversation>> get() = conversationRepository.conversationLiveData

    fun getAllConversations() {
        conversationRepository.getAllConversations()
    }

    fun updateLastMessage(conversationId: String, message: String) {
        conversationRepository.updateLastMessage(conversationId, message)
    }
}