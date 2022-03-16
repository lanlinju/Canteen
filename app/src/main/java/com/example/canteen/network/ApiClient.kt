package com.example.canteen.network

import com.example.canteen.utilities.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null
        fun getRetrofit(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(Constants.NETWORK_DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
//    companion object {
//        private var INSTANCE: Retrofit? = null
//        fun getInstance(): Retrofit? =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Retrofit.Builder()
//                    .baseUrl("http://192.168.1.5:8081/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build().also {
//                        INSTANCE = it
//                    }
//            }
//    }
}