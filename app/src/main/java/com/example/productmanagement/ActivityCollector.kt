package com.example.productmanagement

import android.app.Activity
//随时退出程序，点击事件中增加ActivityCollector.finishAll()
object ActivityCollector {
    private val activities = ArrayList<Activity>()

    fun addActivity(activity:Activity){
        activities.add(activity)
    }
    fun removeActivity(activity: Activity){
        activities.add(activity)
    }
    fun finishAll(){
        for (activity in activities){
            if (!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}