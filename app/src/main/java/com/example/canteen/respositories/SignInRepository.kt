package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.User
import com.example.canteen.network.ApiClient
import com.example.canteen.network.UserApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.responses.StatusCode
import com.example.canteen.utilities.showLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInRepository {

    private var userApiService: UserApiService =
        ApiClient.getRetrofit()?.create(UserApiService::class.java)!!

    fun signIn(email: String, password: String): LiveData<BaseResponse<User>> {
        val data: MutableLiveData<BaseResponse<User>> = MutableLiveData()
        userApiService.signIn(email, password).enqueue(object : Callback<BaseResponse<User>> {
            override fun onResponse(
                call: Call<BaseResponse<User>>,
                response: Response<BaseResponse<User>>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
                val baseResponse = BaseResponse<User>(StatusCode.NetWorkError)
                t.message?.let {
                    baseResponse.msg = it
                    data.value = baseResponse
                    it.showLog()
                }
            }
        })
        return data
    }

}