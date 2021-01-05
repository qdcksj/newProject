package com.example.productmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.query_manager_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar

class QueryManager : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.query_manager_layout)
        setSupportActionBar(toolbar)

        titleList.setOnClickListener {
            val intent = Intent(this,QueryInOut::class.java)
            startActivity(intent)
        }
        outList.setOnClickListener {
            val intent = Intent(this, QueryOut::class.java)
            startActivity(intent)
        }
        inList.setOnClickListener {
            val intent = Intent(this, QueryIn::class.java)
            startActivity(intent)
        }
        finishBtm.setOnClickListener {
            ActivityCollector.finishAll()
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