package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.models.User
import com.example.canteen.network.ApiClient
import com.example.canteen.network.UserApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.utilities.showLog
import com.example.canteen.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body

class UserRepository {

    private val userApiService = ApiClient.getRetrofit()?.create(UserApiService::class.java)!!
    private var _userLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: LiveData<User> get() = _userLiveData
    private val _userListLiveData = MutableLiveData<List<User>>()
    val userListLiveData: LiveData<List<User>> get() = _userListLiveData


    fun getUserById(userId: String) {
        userApiService.getUerById(userId).enqueue(object : Callback<BaseResponse<User>> {
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

    fun getAllUsers() {
        userApiService.getAllUsers().enqueue(object : Callback<BaseResponse<List<User>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<User>>>,
                response: Response<BaseResponse<List<User>>>
            ) {
                response.body()?.let {
                    _userListLiveData.value = it.data
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<User>>>, t: Throwable) {
                t.message?.showToast()
            }
        })
    }

    suspend fun updateUser(user: User): BaseResponse<String>? {
        return withContext(Dispatchers.IO) {
            val result = userApiService.updateUser(user)
            if (result.isSuccessful) {
                return@withContext result.body()
            } else {
                val errorMessage = "出错了，状态码：${result.code()},信息：${result.message()}"
                throw Exception(errorMessage)
            }
        }
    }

    suspend fun deleteUser(id: String): BaseResponse<String>? {
        return withContext(Dispatchers.IO) {
            val result = userApiService.deleteUser(id)
            if (result.isSuccessful) {
                return@withContext result.body()
            } else {
                val errorMessage = "出错了，状态码：${result.code()},信息：${result.message()}"
                throw Exception(errorMessage)
            }
        }
    }
}