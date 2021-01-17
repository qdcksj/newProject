package com.example.productmanagement
/*
*全局获取context
* 2021.1.17@qdzsd
*
 */

import android.app.Application
import android.content.Context

class MyApplication:Application() {
    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}