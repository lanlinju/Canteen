package com.example.canteen.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.canteen.models.Category


class App : Application() {

    var categoryList: List<Category>? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        @SuppressLint("StaticFieldLeak")
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        context = this
    }
}