package com.example.productmanagement

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.content.contentValuesOf
import kotlinx.android.synthetic.main.add_produ_name.*
import kotlinx.android.synthetic.main.super_manager_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import org.jetbrains.anko.selector
import java.sql.ResultSet
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
            val nameAddType = typeName.text
            if (nameAddType == "瓶坯注塑"){
                listZhusuProduName()
            }else if (nameAddType == "非瓶坯注塑"){
                listZhusuOtherName()
            }else if (nameAddType == "吹塑"){
                listChuisuProduName()
            }else if (nameAddType == "挤出"){
                listJichuProduName()
            }else if (nameAddType == "其他"){
                listOtherProduName()
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
    fun initTypeName(){
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
    fun listZhusuProduName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusu", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            var nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            listProduNMView.setOnItemClickListener { _, _, position, _ ->
                val name = nameList[position]
                delProduName.setOnClickListener {
                    db.delete("nativeZhusu", "produName == ?", arrayOf(name))
                    nameList.remove(name)
                    myAdapter.notifyDataSetChanged()
                    try {
                        Thread ({
                            val conn = DBUtil().conection()
                            val sql = "delete from  zhusuprodunametable where name = $name "
                            try {
                                // 创建用来执行sql语句的对象
                                val statement: Statement = conn!!.createStatement()
                                // 执行sql查询语句并获取查询信息
                                val num = statement.executeUpdate(sql)
                                if (num > 0){
                                    Log.d("AddProduName","已从远程注塑库删除")
                                }else{
                                    Log.d("AddProduName","未从远程注塑库删除")
                                }
                            } catch (e: SQLException) {
                                Log.e("MakeManager", "远程注塑名称删除失败")
                            }

                            //关闭数据库
                            try {
                                conn!!.close()
                                Log.d("MakeManager", "关闭连接成功。")
                            } catch (e: SQLException) {
                                Log.d("MakeManager", "关闭连接失败。")
                            }

                        }).start()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
    fun listZhusuOtherName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusuOther", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            var nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            listProduNMView.setOnItemClickListener { _, _, position, _ ->
                val name = nameList[position]
                delProduName.setOnClickListener {
                    db.delete("nativeZhusuOther", "produName == ?", arrayOf(name))
                    nameList.remove(name)
                    myAdapter.notifyDataSetChanged()
                    try {
                        Thread ({
                            val conn = DBUtil().conection()
                            val sql = "delete from  zhusuothertable where name = $name "
                            try {
                                // 创建用来执行sql语句的对象
                                val statement: Statement = conn!!.createStatement()
                                // 执行sql查询语句并获取查询信息
                                val num = statement.executeUpdate(sql)
                                if (num > 0){
                                    Log.d("AddProduName","已从远程注塑库删除")
                                }else{
                                    Log.d("AddProduName","未从远程注塑库删除")
                                }
                            } catch (e: SQLException) {
                                Log.e("MakeManager", "远程注塑名称删除失败")
                            }

                            //关闭数据库
                            try {
                                conn!!.close()
                                Log.d("MakeManager", "关闭连接成功。")
                            } catch (e: SQLException) {
                                Log.d("MakeManager", "关闭连接失败。")
                            }

                        }).start()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    //依据工序名称查询显示吹塑产品名称，要有排序
    fun listChuisuProduName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeChuisu", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            var nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            listProduNMView.setOnItemClickListener { _, _, position, _ ->
                val name = nameList[position]
                delProduName.setOnClickListener {
                    db.delete("nativeChuisu", "produName == ?", arrayOf(name))
                    nameList.remove(name)
                    myAdapter.notifyDataSetChanged()
                    try {
                        Thread ({
                            val conn = DBUtil().conection()
                            val sql = "delete from  chuisuprodunametable where name = $name "
                            try {
                                // 创建用来执行sql语句的对象
                                val statement: Statement = conn!!.createStatement()
                                // 执行sql查询语句并获取查询信息
                                val num = statement.executeUpdate(sql)
                                if (num > 0){
                                    Log.d("AddProduName","已从远程吹塑库删除")
                                }else{
                                    Log.d("AddProduName","未从远程吹塑库删除")
                                }
                            } catch (e: SQLException) {
                                Log.e("MakeManager", "远程吹塑名称删除失败")
                            }

                            //关闭数据库
                            try {
                                conn!!.close()
                                Log.d("MakeManager", "关闭连接成功。")
                            } catch (e: SQLException) {
                                Log.d("MakeManager", "关闭连接失败。")
                            }

                        }).start()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    //依据工序名称查询显示挤出产品名称，要有排序
    fun listJichuProduName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeJichu", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            var nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            listProduNMView.setOnItemClickListener { _, _, position, _ ->
                val name = nameList[position]
                delProduName.setOnClickListener {
                    db.delete("nativeJichu", "produName == ?", arrayOf(name))
                    nameList.remove(name)
                    myAdapter.notifyDataSetChanged()
                    try {
                        Thread ({
                            val conn = DBUtil().conection()
                            val sql = "delete from  jichuprodunametable where name = $name "
                            try {
                                // 创建用来执行sql语句的对象
                                val statement: Statement = conn!!.createStatement()
                                // 执行sql查询语句并获取查询信息
                                val num = statement.executeUpdate(sql)
                                if (num > 0){
                                    Log.d("AddProduName","已从远程吹塑库删除")
                                }else{
                                    Log.d("AddProduName","未从远程吹塑库删除")
                                }
                            } catch (e: SQLException) {
                                Log.e("MakeManager", "远程吹塑名称删除失败")
                            }

                            //关闭数据库
                            try {
                                conn!!.close()
                                Log.d("MakeManager", "关闭连接成功。")
                            } catch (e: SQLException) {
                                Log.d("MakeManager", "关闭连接失败。")
                            }

                        }).start()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    //依据工序名称查询显示挤出产品名称，要有排序
    fun listOtherProduName(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeOther", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            var nameList: ArrayList<String> = ArrayList()
            nameList.sort()//排序
            val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
            listProduNMView.adapter = myAdapter  //列表显示

            listProduNMView.setOnItemClickListener { _, _, position, _ ->
                val name = nameList[position]
                delProduName.setOnClickListener {
                    db.delete("nativeOther", "produName == ?", arrayOf(name))
                    nameList.remove(name)
                    myAdapter.notifyDataSetChanged()
                    try {
                        Thread ({
                            val conn = DBUtil().conection()
                            val sql = "delete from  otherprodunametable where name = $name "
                            try {
                                // 创建用来执行sql语句的对象
                                val statement: Statement = conn!!.createStatement()
                                // 执行sql查询语句并获取查询信息
                                val num = statement.executeUpdate(sql)
                                if (num > 0){
                                    Log.d("AddProduName","已从远程其他库删除")
                                }else{
                                    Log.d("AddProduName","未从远程其他库删除")
                                }
                            } catch (e: SQLException) {
                                Log.e("MakeManager", "远程其他库名称删除失败")
                            }

                            //关闭数据库
                            try {
                                conn!!.close()
                                Log.d("MakeManager", "关闭连接成功。")
                            } catch (e: SQLException) {
                                Log.d("MakeManager", "关闭连接失败。")
                            }

                        }).start()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
            do {
                val produName = cursor.getString(cursor.getColumnIndex("produName"))
                nameList.add(produName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    //增加新产品
    fun addNewProdu(){
        val nameType = typeName.text
        if (nameType == "瓶坯注塑"){
            addNewZhusu()
        }else if (nameType == "非瓶坯注塑"){
            addNewOtherZhusu()
        }else if (nameType == "吹塑"){
            addNewChuisu()
        }else if (nameType == "挤出"){
            addNewJichu()
        }else if (nameType == "其他"){
            addNewOther()
        }

    }
    //注塑品名增加
    fun addNewZhusu(){
        val newName = editNewPM.text
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeZhusu", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread ({
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

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun addNewOtherZhusu(){
        val newName = editNewPM.text
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeZhusu", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread ({
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

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun addNewChuisu(){
        val newName = editNewPM.text
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeChuisu", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread ({
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

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun addNewJichu(){
        val newName = editNewPM.text
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeJichu", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread ({
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

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun addNewOther(){
        val newName = editNewPM.text
        val db = dbHelper.writableDatabase
        val values = contentValuesOf("produName" to newName)
        db.insert("nativeOther", null, values)
        Log.d("AddProduName","已添加到本地注塑库")
        try {
            Thread ({
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

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    //删除选中的产品
    fun delSelectProdu(){

    }
}