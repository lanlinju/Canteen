package com.example.canteen.network

import com.example.canteen.responses.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApiService {
    @Multipart
    @POST("file")//请求方法为POST，里面为你要上传的url
    //注解用@Part，参数类型为List<MultipartBody.Part> 方便上传其它需要的参数或多张图片
    fun uploadFile(@Part partList:List<MultipartBody.Part> ): Call<BaseResponse<String>>
}