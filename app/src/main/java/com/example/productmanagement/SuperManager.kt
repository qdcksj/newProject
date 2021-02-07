package com.example.productmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.super_manager_layout.*

class SuperManager : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.super_manager_layout)
        setSupportActionBar(toolbar)

        intoStore.setOnClickListener {
             val intent = Intent(this,InBound::class.java)
            startActivity(intent)
        }
        outStore.setOnClickListener {
              val intent = Intent(this,OutBound::class.java)
            startActivity(intent)
        }
        editData.setOnClickListener {
              val intent = Intent(this,UpDataManager::class.java)
            startActivity(intent)
        }
        listData.setOnClickListener {
              val intent = Intent(this,QueryManager::class.java)
            startActivity(intent)
        }
        //程序管理，暂无内容
        managerData.setOnClickListener {

        }




    }
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