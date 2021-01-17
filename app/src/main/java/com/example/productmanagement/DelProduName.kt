package com.example.productmanagement


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.add_produ_name.toolbar
import kotlinx.android.synthetic.main.del_produ_name.*
import org.jetbrains.anko.selector
import java.sql.SQLException
import java.sql.Statement

class DelProduName : BaseActivity() {
    private val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
    private val nameList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.del_produ_name)
        setSupportActionBar(toolbar)
        initTypeName()

        listProduNameWork.setOnClickListener {
            nameList.clear()
            when(typeNameWork.text){
                "瓶坯注塑" ->{
                    val db = dbHelper.writableDatabase
                    val cursor = db.query("nativeZhusu", null, null, null, null, null, null)
                    if (cursor.moveToFirst()) {
                        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
                        myAdapter.notifyDataSetChanged()
                        listProduViewWork.adapter = myAdapter  //列表显示
                        listProduViewWork.setOnItemClickListener { _, _, position, _ ->
                            val name = nameList[position]
                            delProduWork.setOnClickListener {
                                db.delete("nativeZhusu", "produName == ?", arrayOf(name))
                                nameList.remove(name)
                                myAdapter.notifyDataSetChanged()
                                try {
                                    Thread {
                                        val conn = DBUtil().conection()
                                        val sql = "delete from  zhusuprodunametable where name = '$name' "
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

                                    }.start()
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                            "瓶坯名称已删除！".showToast()
                        }
                        do {
                            val produName = cursor.getString(cursor.getColumnIndex("produName"))
                            nameList.add(produName)
                            nameList.sort()
                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                }
                "非瓶坯注塑" ->{

                    val db = dbHelper.writableDatabase
                    val cursor = db.query("nativeZhusuOther", null, null, null, null, null, null)
                    if (cursor.moveToFirst()) {
                        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
                        listProduViewWork.adapter = myAdapter  //列表显示
                        listProduViewWork.setOnItemClickListener { _, _, position, _ ->
                            val name = nameList[position]
                            delProduWork.setOnClickListener {
                                db.delete("nativeZhusuOther", "produName == ?", arrayOf(name))
                                nameList.remove(name)
                                myAdapter.notifyDataSetChanged()
                                try {
                                    Thread {
                                        val conn = DBUtil().conection()
                                        val sql = "delete from  zhusuothertable where name = '$name' "
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

                                    }.start()
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                            "注塑件名称已删除！".showToast()
                        }
                        do {
                            val produName = cursor.getString(cursor.getColumnIndex("produName"))
                            nameList.add(produName)
                            nameList.sort()
                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                }
                "吹塑" ->{

                    val db = dbHelper.writableDatabase
                    val cursor = db.query("nativeChuisu", null, null, null, null, null, null)
                    if (cursor.moveToFirst()) {
                        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
                        listProduViewWork.adapter = myAdapter  //列表显示
                        listProduViewWork.setOnItemClickListener { _, _, position, _ ->
                            val name = nameList[position]
                            delProduWork.setOnClickListener {
                                db.delete("nativeChuisu", "produName == ?", arrayOf(name))
                                nameList.remove(name)
                                myAdapter.notifyDataSetChanged()
                                try {
                                    Thread {
                                        val conn = DBUtil().conection()
                                        val sql = "delete from  chuisuprodunametable where name = '$name' "
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

                                    }.start()
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                            "吹塑名称已删除！".showToast()
                        }
                        do {
                            val produName = cursor.getString(cursor.getColumnIndex("produName"))
                            nameList.add(produName)
                            nameList.sort()
                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                }
                "挤出" ->{

                    val db = dbHelper.writableDatabase
                    val cursor = db.query("nativeJichu", null, null, null, null, null, null)
                    if (cursor.moveToFirst()) {
                        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
                        listProduViewWork.adapter = myAdapter  //列表显示
                        listProduViewWork.setOnItemClickListener { _, _, position, _ ->
                            val name = nameList[position]
                            delProduWork.setOnClickListener {
                                db.delete("nativeJichu", "produName == ?", arrayOf(name))
                                nameList.remove(name)
                                myAdapter.notifyDataSetChanged()
                                try {
                                    Thread {
                                        val conn = DBUtil().conection()
                                        val sql = "delete from  jichuprodunametable where name = '$name' "
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

                                    }.start()
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                            "挤出产品名称已删除！".showToast()
                        }
                        do {
                            val produName = cursor.getString(cursor.getColumnIndex("produName"))
                            nameList.add(produName)
                            nameList.sort()
                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                }
                "其他" ->{

                    val db = dbHelper.writableDatabase
                    val cursor = db.query("nativeOther", null, null, null, null, null, null)
                    if (cursor.moveToFirst()) {
                        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList)
                        listProduViewWork.adapter = myAdapter  //列表显示
                        listProduViewWork.setOnItemClickListener { _, _, position, _ ->
                            val name = nameList[position]
                            delProduWork.setOnClickListener {
                                db.delete("nativeOther", "produName == ?", arrayOf(name))
                                nameList.remove(name)
                                myAdapter.notifyDataSetChanged()
                                try {
                                    Thread {
                                        val conn = DBUtil().conection()
                                        val sql = "delete from  otherprodunametable where name = '$name' "
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

                                    }.start()
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                            "其他产品名称已删除！".showToast()
                        }
                        do {
                            val produName = cursor.getString(cursor.getColumnIndex("produName"))
                            nameList.add(produName)
                            nameList.sort()
                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                }
            }
        }

    }


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
        typeNameWork.text = ""
        typeNameWork.setOnClickListener {
            selector("选择工序名称", typeList){i ->
                typeNameWork.text = typeList[i]
            }
        }
    }



//toolBar点击事件
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