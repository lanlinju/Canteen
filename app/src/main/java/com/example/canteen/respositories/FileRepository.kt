package com.example.canteen.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.canteen.network.ApiClient
import com.example.canteen.network.FileApiService
import com.example.canteen.responses.BaseResponse
import com.example.canteen.utilities.showLog
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FileRepository {
    private val fileApiService = ApiClient.getRetrofit()?.create(FileApiService::class.java)!!
    private val _uploadInfo = MutableLiveData<String>()
    val uploadInfo: LiveData<String> get() = _uploadInfo

    fun uploadFile(path: String) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)//表单类型
        val file = File(path);
        val body = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())//表单类型
        /**
         * ps:builder.addFormDataPart("code","123456");
         * ps:builder.addFormDataPart("file",file.getName(),body);
         */
        //builder.addFormDataPart(key, value);//传入服务器需要的key，和相应value值
        builder.addFormDataPart("myfile", file.name, body); //添加图片数据，body创建的请求体
        val parts = builder.build().parts
        fileApiService.uploadFile(parts).enqueue(object : Callback<BaseResponse<String>> {
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>
            ) {
                _uploadInfo.value = response.body()?.data
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                t.message?.showLog()
            }
        })
    }

}