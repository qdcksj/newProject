package com.example.productmanagement

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
//让所有的activity都继承自本类，可以知道当前是哪一个activity
open class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BaseActivity", javaClass.simpleName)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}