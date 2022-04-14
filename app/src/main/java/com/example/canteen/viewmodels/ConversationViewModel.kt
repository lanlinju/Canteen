package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canteen.models.Conversation
import com.example.canteen.respositories.ConversationRepository
import com.example.canteen.utilities.showLog
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun getConversationsByUserId(userId: String) {
        conversationRepository.getConversationsByUserId(userId)
    }

    fun updateLastMessage(conversationId: String, message: String, dateTime: Date) {
        conversationRepository.updateLastMessage(conversationId, message, dateTime)
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            try {
                flow {
                    val info = conversationRepository.deleteConversation(id)
                    emit(info!!)
                }.collect {
                    "删除成功：$it".showLog()
                    it.showLog()
                }
            }catch (e:Exception){
                e.message?.showToast()
            }

        }

    }
}