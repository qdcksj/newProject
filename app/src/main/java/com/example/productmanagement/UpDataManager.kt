package com.example.productmanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import kotlinx.android.synthetic.main.up_data_manager_layout.*

class UpDataManager : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.up_data_manager_layout)
        setSupportActionBar(toolbar)

        addNewProdu.setOnClickListener {
            val intent = Intent(this,AddProduName::class.java)
            startActivity(intent)
        }
        addProduColor.setOnClickListener {
            val intent = Intent(this, AddColorName::class.java)
            startActivity(intent)
        }

        delColorWork.setOnClickListener {
            val intent = Intent(this, DelColor::class.java)
            startActivity(intent)
        }
        delProduName.setOnClickListener {
            val intent = Intent(this, DelProduName::class.java)
            startActivity(intent)
        }

    }
    //toolbar点击事件
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.backup ->finish()
            R.id.finish ->ActivityCollector.finishAll()
        }
        return true
    }
}
