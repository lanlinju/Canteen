package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.User
import com.example.canteen.respositories.SignInRepository

class SignInViewModel: ViewModel() {
//    private val mUserLiveData = MutableLiveData<User>()
//    val userLiveData: LiveData<User> get() = mUserLiveData
    private val signInViewRepository = SignInRepository()

    fun signIn(email: String, password: String):LiveData<User>{
        return signInViewRepository.signIn(email, password)
    }
}