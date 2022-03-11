package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.User
import com.example.canteen.network.ApiClient
import com.example.canteen.network.UserApiService
import com.example.canteen.responses.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpRepository {

    private var userApiService: UserApiService = ApiClient.getRetrofit()?.create(UserApiService::class.java)!!

    fun signUp(user: User): LiveData<BaseResponse<String>> {
        val data: MutableLiveData<BaseResponse<String>> = MutableLiveData()
        userApiService.signUp(user).enqueue(object : Callback<BaseResponse<String>>{
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                data.value = null
            }
        })
          return data
    }
}