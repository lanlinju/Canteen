package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.User
import com.example.canteen.respositories.UserRepository


class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()
     var isLoaded = false
    val userLiveData: LiveData<User> get() = userRepository.userLiveData

    fun getUserById(userId: String) {
        userRepository.getUserById(userId)
    }

}