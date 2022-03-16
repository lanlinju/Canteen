package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.models.User
import com.example.canteen.responses.BaseResponse
import com.example.canteen.respositories.SignUpRepository


class SignUpViewModel: ViewModel() {

    private var signUpRepository:SignUpRepository = SignUpRepository()

    fun signUp(user: User): LiveData<BaseResponse<String>> {
        return signUpRepository.signUp(user)
    }
}