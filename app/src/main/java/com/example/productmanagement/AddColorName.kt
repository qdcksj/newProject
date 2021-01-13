package com.example.productmanagement


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.content.contentValuesOf
import kotlinx.android.synthetic.main.add_color_name.*
import kotlinx.android.synthetic.main.add_color_name.backBtm
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import java.sql.SQLException
import java.sql.Statement

class AddColorName : BaseActivity() {
    val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_color_name)
        setSupportActionBar(toolbar)

        addNewColorPM.setOnClickListener {
            addNewColor()
            "添加颜色成功！".showToast()
        }
        listColorPM.setOnClickListener {
            listColorName()
        }

        //返回上级页面
        backBtm.setOnClickListener {
            finish()
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

    private fun addNewColor(){
        val newName = editNewColorPM.text.toString()
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produColor" to newName)
        db.insert("nativePruoduColor",null,values)
        Log.d("AddColorName","已添加到本地注塑库")
        try {
            Thread {
                val conn = DBUtil().conection()
                val sql = "insert into colornametable values (null,'$newName') "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val num = statement.executeUpdate(sql)
                    if (num > 0){
                        Log.d("AddColorName","已添加到远程注塑库")
                    }
                } catch (e: SQLException) {
                   // Log.e("AddColorName", "远程注塑名称插入失败")
                }

                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("AddColorName", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("AddColorName", "关闭连接失败。")
                }

            }.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private fun listColorName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeProduColor", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listColorNMView.adapter = myAdapter  /* 列表显示 */
            do {
                val colorName = cursor.getString(cursor.getColumnIndex("produColor"))
                nameList.add(colorName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
}