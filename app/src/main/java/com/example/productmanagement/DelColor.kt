package com.example.productmanagement


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.del_color_layout.*
import kotlinx.android.synthetic.main.del_color_layout.toolbar
import java.sql.SQLException
import java.sql.Statement

class DelColor : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.del_color_layout)
        setSupportActionBar(toolbar)

        val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeProduColor", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val nameList1 = ArrayList<String>()
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList1)
            myAdapter.notifyDataSetChanged()
            listColorView1.adapter = myAdapter  //列表显示
            listColorView1.setOnItemClickListener { _, _, position, _ ->
                val name = nameList1[position]
                delColor.setOnClickListener {
                    db.delete("nativeProduColor", "produColor == ?", arrayOf(name))
                    nameList1.remove(name)
                    myAdapter.notifyDataSetChanged()
                    try {
                        Thread {
                            val conn = DBUtil().conection()
                            val sql = "delete from  colornametable where name = '$name' "
                            try {
                                // 创建用来执行sql语句的对象
                                val statement: Statement = conn!!.createStatement()
                                // 执行sql查询语句并获取查询信息
                                val num = statement.executeUpdate(sql)
                                if (num > 0){
                                    Log.d("AddColorName","已从远程颜色库删除")
                                }else{
                                    Log.d("AddColorName","未从远程颜色库删除")
                                }
                            } catch (e: SQLException) {
                                Log.e("AddColorName", "远程颜色名称删除失败")
                            }

                            //关闭数据库
                            try {
                                conn!!.close()
                                Log.d("AddColorName", "关闭连接成功。")
                            } catch (e: SQLException) {
                                Log.d("AddColorName", "关闭连接失败。")
                            }

                        }.start()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

            }
            do {
                val colorName = cursor.getString(cursor.getColumnIndex("produColor"))
                nameList1.add(colorName)
                nameList1.sort()
            } while (cursor.moveToNext())
        }
        cursor.close()
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