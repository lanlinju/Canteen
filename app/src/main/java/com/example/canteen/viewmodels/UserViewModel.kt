package com.example.canteen.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canteen.models.User
import com.example.canteen.respositories.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()
    var isLoaded = false
    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility = _progressBarVisibility
    val userLiveData: LiveData<User> get() = userRepository.userLiveData
    val userListLiveData: LiveData<List<User>> get() = userRepository.userListLiveData

    fun getUserById(userId: String) {
        userRepository.getUserById(userId)
    }

    fun getAllUsers() {
        userRepository.getAllUsers()
    }

    fun toggleProgressBarVisibility() {
        if (_progressBarVisibility.value == View.INVISIBLE) {
            _progressBarVisibility.value = View.VISIBLE
        } else {
            _progressBarVisibility.value = View.INVISIBLE
        }
    }
}