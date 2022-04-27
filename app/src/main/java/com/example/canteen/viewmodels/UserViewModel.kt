package com.example.canteen.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canteen.models.User
import com.example.canteen.respositories.UserRepository
import com.example.canteen.utilities.showLog
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
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

    fun updateUser(user: User) {
        viewModelScope.launch {
            flow {
                try {
                    val info = userRepository.updateUser(user)
                    emit(info)
                } catch (e: Exception) {
                    e.message?.showToast()
                }
            }.collect {
                if (it?.code == 0) {
                    "更新成功".showToast()
                } else {
                    "更新失败".showToast()
                }
            }
        }
    }

    fun deleteUser(id:String)  {
        viewModelScope.launch {
            flow {
                try {
                    val info =  userRepository.deleteUser(id)
                    emit(info)
                } catch (e: Exception) {
                    e.message?.showToast()
                }
            }.collect {
                if (it?.data == "1") {
                    "删除成功".showToast()
                } else {
                    "删除失败".showToast()
                }
            }
        }
    }

    fun toggleProgressBarVisibility() {
        if (_progressBarVisibility.value == View.INVISIBLE) {
            _progressBarVisibility.value = View.VISIBLE
        } else {
            _progressBarVisibility.value = View.INVISIBLE
        }
    }
}