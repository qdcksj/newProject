package com.example.productmanagement

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class QueryChuisuAdapter(activity: Activity, val resouceId:Int, data:List<QueryChuisuProdu>):
    ArrayAdapter<QueryChuisuProdu>(activity, resouceId, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resouceId, parent,false)
        val queryDate: TextView = view.findViewById(R.id.queryChuisuDateView)
        val queryAmount: TextView = view.findViewById(R.id.queryChuisuAmountView)
        val queryPingpi:TextView = view.findViewById(R.id.queryPingpiView)
        val queryMenu: TextView = view.findViewById(R.id.queryChuisuMenuView)

        val queryChuisuProdu = getItem(position)

        if (queryChuisuProdu != null){
            queryDate.text = queryChuisuProdu.queryDate.toString()
            queryAmount.text = queryChuisuProdu.queryAmount.toString()
            queryPingpi.text = queryChuisuProdu.pingpiNm
            queryMenu.text = queryChuisuProdu.queryMenu
        }
        return view
    }
}