package com.example.canteen.viewmodels

import androidx.lifecycle.ViewModel
import com.example.canteen.models.Chat
import com.example.canteen.respositories.ChatRepository

class ChatViewModel:ViewModel() {
    private val chatRepository = ChatRepository()
    fun insertChat(chat: Chat){
        chatRepository.insertChat(chat)
    }
}