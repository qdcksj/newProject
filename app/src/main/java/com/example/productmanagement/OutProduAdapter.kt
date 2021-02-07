package com.example.productmanagement

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class OutProduAdapter(activity: Activity, private val resouceId:Int, data: ArrayList<OutProdu>):
    ArrayAdapter<OutProdu>(activity, resouceId, data) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resouceId, parent,false)
        val typeName: TextView = view.findViewById(R.id.typeView)
        val produName:TextView = view.findViewById(R.id.nameView)
        val produColor:TextView = view.findViewById(R.id.colorView)
        val outAmount:TextView = view.findViewById(R.id.amountView)
        val produMenu: TextView = view.findViewById(R.id.menuView)

        val outProdu = getItem(position)

        if (outProdu != null){
            typeName.text = outProdu.typeName
            produName.text = outProdu.produName
            produColor.text = outProdu.produColor
            outAmount.text = outProdu.outAmount
            produMenu.text = outProdu.produMenu
        }
        return view
    }
}