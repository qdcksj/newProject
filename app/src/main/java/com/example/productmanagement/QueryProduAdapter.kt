package com.example.productmanagement

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class QueryProduAdapter(activity: Activity, val resouceId:Int, data:List<QueryProdu>):
    ArrayAdapter<QueryProdu>(activity, resouceId, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resouceId, parent,false)
        val queryDate: TextView = view.findViewById(R.id.queryDateView)
        val queryAmount: TextView = view.findViewById(R.id.queryAmountView)
        val queryMenu: TextView = view.findViewById(R.id.queryMenuView)
        val queryProdu = getItem(position)

        if (queryProdu != null){
            queryDate.text = queryProdu.queryDate
            queryAmount.text = queryProdu.queryAmount
            queryMenu.text = queryProdu.queryMenu
        }
        return view
    }
}