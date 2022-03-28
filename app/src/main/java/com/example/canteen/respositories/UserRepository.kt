package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.User
import com.example.canteen.network.ApiClient
import com.example.canteen.network.UserApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.utilities.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    private val userApiService = ApiClient.getRetrofit()?.create(UserApiService::class.java)
    var _userLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData : LiveData<User> get() = _userLiveData


    fun getUserById(userId: String) {
        userApiService?.getUerById(userId)?.enqueue(object : Callback<BaseResponse<User>> {
            override fun onResponse(
                call: Call<BaseResponse<User>>,
                response: Response<BaseResponse<User>>
            ) {
                response.body()?.let {
                    if (it.code == -1) {
                        "用户不存在".showToast()
                        return@let
                    }
                    _userLiveData.value = it.data
                }
            }

            override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
                t.message?.showToast()
            }
        })
    }
}