package com.example.canteen.utilities

import android.util.Log

class MyLog {
    companion object {
        fun showLog(message: String) {
            Log.d("TAG", message)
        }
    }
}