package com.example.productmanagement


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.content.contentValuesOf
import kotlinx.android.synthetic.main.add_produ_name.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import org.jetbrains.anko.selector
import java.sql.SQLException
import java.sql.Statement

class AddProduName : BaseActivity() {
    val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_produ_name)
        setSupportActionBar(toolbar)
        initTypeName()//初始化工序名称

        //添加产品按键点击
        addNewPM.setOnClickListener {
            addNewProdu()
        }

        //显示产品目录点击
        listPM.setOnClickListener {
            when (typeName.text) {
                "瓶坯注塑" -> {
                    listZhusuProduName()
                }
                "非瓶坯注塑" -> {
                    listZhusuOtherName()
                }
                "吹塑" -> {
                    listChuisuProduName()
                }
                "挤出" -> {
                    listJichuProduName()
                }
                "其他" -> {
                    listOtherProduName()
                }
            }
        }
        //返回上级页面按键
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

    //初始化工序名称
    private fun initTypeName(){
        val typeList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeType",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val typeName = cursor.getString(cursor.getColumnIndex("typeName"))
                typeList.add(typeName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        typeName.text = typeList[0]
        typeName.setOnClickListener {
            selector("选择工序名称", typeList){i ->  
                typeName.text = typeList[i]
            }
        }
    }
    //依据工序名称查询显示注塑产品名称，要有排序
    private fun listZhusuProduName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusu", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
    private fun listZhusuOtherName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusuOther", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    //依据工序名称查询显示吹塑产品名称，要有排序
    private fun listChuisuProduName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeChuisu", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    //依据工序名称查询显示挤出产品名称，要有排序
    private fun listJichuProduName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeJichu", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    //依据工序名称查询显示挤出产品名称，要有排序
    private fun listOtherProduName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeOther", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    //增加新产品
    private fun addNewProdu(){
        when (typeName.text) {
            "瓶坯注塑" -> {
                addNewZhusu()
            }
            "非瓶坯注塑" -> {
                addNewOtherZhusu()
            }
            "吹塑" -> {
                addNewChuisu()
            }
            "挤出" -> {
                addNewJichu()
            }
            "其他" -> {
                addNewOther()
            }
        }

    }
    //注塑品名增加
    private fun addNewZhusu(){
        val newName = editNewPM.text.toString()
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeZhusu", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread {
                val conn = DBUtil().conection()
                val sql = "insert into zhusuprodunametable values (null,'$newName') "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val num = statement.executeUpdate(sql)
                    if (num > 0){
                        Log.d("AddProduName","已添加到远程注塑库")
                    }else{
                        Log.d("AddProduName","已添加到远程注塑库")
                    }
                } catch (e: SQLException) {
                    Log.e("MakeManager", "远程注塑名称插入失败")
                }

                //关闭数据库
                try {
                    conn!!.close()
                    Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    Log.d("MakeManager", "关闭连接失败。")
                }

            }.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun addNewOtherZhusu(){
        val newName = editNewPM.text.toString()
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeZhusu", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread {
                val conn = DBUtil().conection()
                val sql = "insert into zhusuothertable values (null,'$newName') "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val num = statement.executeUpdate(sql)
                    if (num > 0){
                        Log.d("AddProduName","已添加到远程注塑库")
                    }else{
                        Log.d("AddProduName","已添加到远程注塑库")
                    }
                } catch (e: SQLException) {
                    Log.e("MakeManager", "远程注塑名称插入失败")
                }

                //关闭数据库
                try {
                    conn!!.close()
                    Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    Log.d("MakeManager", "关闭连接失败。")
                }

            }.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun addNewChuisu(){
        val newName = editNewPM.text.toString()
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeChuisu", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread {
                val conn = DBUtil().conection()
                val sql = "insert into chuisuprodunametable values (null,'$newName') "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val num = statement.executeUpdate(sql)
                    if (num > 0){
                        Log.d("AddProduName","已添加到远程吹塑库")
                    }else{
                        Log.d("AddProduName","已添加到远程吹塑库")
                    }
                } catch (e: SQLException) {
                    Log.e("MakeManager", "远程吹塑名称插入失败")
                }

                //关闭数据库
                try {
                    conn!!.close()
                    Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    Log.d("MakeManager", "关闭连接失败。")
                }

            }.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun addNewJichu(){
        val newName = editNewPM.text.toString()
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeJichu", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread {
                val conn = DBUtil().conection()
                val sql = "insert into jichuprodunametable values (null,'$newName') "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val num = statement.executeUpdate(sql)
                    if (num > 0){
                        Log.d("AddProduName","已添加到远程注塑库")
                    }else{
                        Log.d("AddProduName","已添加到远程注塑库")
                    }
                } catch (e: SQLException) {
                    Log.e("MakeManager", "远程注塑名称插入失败")
                }

                //关闭数据库
                try {
                    conn!!.close()
                    Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    Log.d("MakeManager", "关闭连接失败。")
                }

            }.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun addNewOther(){
        val newName = editNewPM.text.toString()
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeOther", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread {
                val conn = DBUtil().conection()
                val sql = "insert into otherprodunametable values (null,'$newName') "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val num = statement.executeUpdate(sql)
                    if (num > 0){
                        Log.d("AddProduName","已添加到远程注塑库")
                    }else{
                        Log.d("AddProduName","已添加到远程注塑库")
                    }
                } catch (e: SQLException) {
                    Log.e("MakeManager", "远程注塑名称插入失败")
                }

                //关闭数据库
                try {
                    conn!!.close()
                    Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    Log.d("MakeManager", "关闭连接失败。")
                }

            }.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}