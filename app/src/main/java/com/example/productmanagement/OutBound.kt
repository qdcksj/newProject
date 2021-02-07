package com.example.productmanagement
/*
*产品出库操作
* 2021.1.17@qdzsd
*
 */
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.contentValuesOf
import com.example.productmanagement.myclass.SearchAdapter
import kotlinx.android.synthetic.main.out_bound_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import org.jetbrains.anko.selector
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class OutBound : BaseActivity(){
    val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.out_bound_layout)
        setSupportActionBar(toolbar)
        initTypeName()
        initColorName()
        date_textT.text = Date().getNowDate()

         addButton.setOnClickListener {
             initAddTemp()
             initQueryTemp()
         }
        OkButton.setOnClickListener {
            outSave()
            "产品出库成功！".showToast()
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
        produTypeName.text = ""
        produTypeName.setOnClickListener {
            selector("选择工序名称", typeList){i ->
                produTypeName.text = typeList[i]

                when (produTypeName.text) {
                    "瓶坯注塑" -> {
                        reFresh()
                        initZhusuName()
                        outPingpiSearch.setText("不使用瓶坯")
                    }
                    "非瓶坯注塑" -> {
                        reFresh()
                        initOtherZhusuName()
                        outPingpiSearch.setText("不使用瓶坯")
                    }
                    "吹塑" -> {
                        reFresh()
                        initChuisuName()
                        initPingpiName()
                    }
                    "挤出" -> {
                        reFresh()
                        initJichuName()
                        outPingpiSearch.setText("不使用瓶坯")
                    }
                    "其他" -> {
                        reFresh()
                        initOtherName()
                        outPingpiSearch.setText("不使用瓶坯")
                    }
                }

            }
        }
    }

    private fun reFresh(){
        outNameSearch.setText("")
        outColorSearch.setText("")
        outAmount.setText("")
        editMenu.setText("")
    }

    //保存至远程数据库，依据不同类型，保存至不同数据库
    @SuppressLint("Recycle")
    private fun outSave(){
        val user2 = getUser()
        val outProduList = ArrayList<OutProdu>()
        val date = date_textT.text
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeOutTable",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val type = cursor.getString(cursor.getColumnIndex("lineName"))
                val name = cursor.getString(cursor.getColumnIndex("produName"))
                val pingpi = cursor.getString(cursor.getColumnIndex("pingpiName"))
                val color = cursor.getString(cursor.getColumnIndex("produColor"))
                val amount = cursor.getInt(cursor.getColumnIndex("outAmount"))
                val menu = cursor.getString(cursor.getColumnIndex("produMenu"))
                when (type) {
                    "瓶坯注塑" -> {
                        try {
                            Thread {
                                val conn = DBUtil().conection()
                                val sql = "insert into zhusuouttable values (null, '$name', '$color', '$amount', '$date', '$menu','$user2') "
                                try {
                                    // 创建用来执行sql语句的对象
                                    val statement: Statement = conn!!.createStatement()
                                    // 执行sql查询语句并获取查询信息
                                    val num = statement.executeUpdate(sql)
                                    if (num > 0){
                                        Log.d("OutBound","已添加到远程瓶坯注塑出库")
                                    }
                                } catch (e: SQLException) {
                                    //Log.e("OutBound", "远程出库瓶坯注塑名称插入失败")
                                }

                                //关闭数据库
                                try {
                                    conn!!.close()
                                    //Log.d("OutBound", "关闭连接成功。")
                                } catch (e: SQLException) {
                                    //Log.d("OutBound", "关闭连接失败。")
                                }

                            }.start()
                        }catch (e:Exception){
                            e.printStackTrace()
                    }
                }
                    "非瓶坯注塑" -> {
                        try {
                            Thread {
                                val conn = DBUtil().conection()
                                val sql = "insert into zhusuotherouttable values (null, '$name', '$color', '$amount', '$date', '$menu','$user2') "
                                try {
                                    // 创建用来执行sql语句的对象
                                    val statement: Statement = conn!!.createStatement()
                                    // 执行sql查询语句并获取查询信息
                                    val num = statement.executeUpdate(sql)
                                    if (num > 0){
                                        Log.d("OutBound","已添加到远程瓶坯注塑出库")
                                    }
                                } catch (e: SQLException) {
                                    //Log.e("OutBound", "远程出库瓶坯注塑插入失败")
                                }

                                //关闭数据库
                                try {
                                    conn!!.close()
                                    //Log.d("OutBound", "关闭连接成功。")
                                } catch (e: SQLException) {
                                    //Log.d("OutBound", "关闭连接失败。")
                                }

                            }.start()
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    "吹塑" -> {
                        try {
                            Thread {
                                val conn = DBUtil().conection()
                                val sql = "insert into chuisuouttable values (null, '$name','$pingpi', '$color', '$amount', '$date', '$menu','$user2') "
                                try {
                                    // 创建用来执行sql语句的对象
                                    val statement: Statement = conn!!.createStatement()
                                    // 执行sql查询语句并获取查询信息
                                    val num = statement.executeUpdate(sql)
                                    if (num > 0){
                                        Log.d("OutBound","已添加到远程瓶坯吹塑出库")
                                    }
                                } catch (e: SQLException) {
                                    //Log.e("OutBound", "远程出库吹塑插入失败")
                                }

                                //关闭数据库
                                try {
                                    conn!!.close()
                                    //Log.d("OutBound", "关闭连接成功。")
                                } catch (e: SQLException) {
                                    //Log.d("OutBound", "关闭连接失败。")
                                }

                            }.start()
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    "挤出" -> {
                        try {
                            Thread {
                                val conn = DBUtil().conection()
                                val sql = "insert into jichuouttable values (null,'$name', '$color', '$amount', '$date', '$menu','$user2') "
                                try {
                                    // 创建用来执行sql语句的对象
                                    val statement: Statement = conn!!.createStatement()
                                    // 执行sql查询语句并获取查询信息
                                    val num = statement.executeUpdate(sql)
                                    if (num > 0){
                                        Log.d("OutBound","已添加到远程挤出出库")
                                    }
                                } catch (e: SQLException) {
                                    //Log.e("OutBound", "远程出库挤出插入失败")
                                }

                                //关闭数据库
                                try {
                                    conn!!.close()
                                    //Log.d("OutBound", "关闭连接成功。")
                                } catch (e: SQLException) {
                                    //Log.d("OutBound", "关闭连接失败。")
                                }

                            }.start()
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    "其他" -> {
                        try {
                            Thread {
                                val conn = DBUtil().conection()
                                val sql = "insert into otherouttable values (null,'$name','$color', '$amount', '$date', '$menu','$user2') "
                                try {
                                    // 创建用来执行sql语句的对象
                                    val statement: Statement = conn!!.createStatement()
                                    // 执行sql查询语句并获取查询信息
                                    val num = statement.executeUpdate(sql)
                                    if (num > 0){
                                        Log.d("OutBound","已添加到远程其他出库")
                                    }
                                } catch (e: SQLException) {
                                    //Log.e("OutBound", "远程出库其他插入失败")
                                }

                                //关闭数据库
                                try {
                                    conn!!.close()
                                    //Log.d("OutBound", "关闭连接成功。")
                                } catch (e: SQLException) {
                                    //Log.d("OutBound", "关闭连接失败。")
                                }

                            }.start()
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }while (cursor.moveToNext())
        }
        val adapter = OutProduAdapter(this,R.layout.out_produ_item, outProduList)
        tempListView.adapter = adapter
        db.delete("nativeOutTable",null,null )
        outProduList.clear()
        adapter.notifyDataSetChanged()
       initTypeName()
    }
    //添加到临时数据库
    private fun initAddTemp(){
        val line = produTypeName.text.toString()
        val produName = outNameSearch.text.toString()
        val pingpiNM = outPingpiSearch.text.toString()
        val colorNM = outColorSearch.text.toString()
        val outPro = outAmount.text.toString()
        val menuPro = editMenu.text.toString()

        val db = dbHelper.writableDatabase

        val values = contentValuesOf("lineName" to line,"produName" to " $produName", "pingpiName" to pingpiNM, "produColor" to colorNM,
            "outAmount" to outPro, "produMenu" to menuPro
        )
        db.insert("nativeOutTable", null, values)
        //Log.d("OutBound", "本地临时库已更新")
    }

    //查询临时数据库并列表显示
    private fun initQueryTemp(){
        val outProduList = ArrayList<OutProdu>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeOutTable",null,null,null,null,null,null)
        if (cursor.moveToFirst()){

            val adapter = OutProduAdapter(this,R.layout.out_produ_item, outProduList)
            tempListView.adapter = adapter
            //Log.d("OutBound", "列表已更新")
            tempListView.setOnItemClickListener { _, _, position, _ ->
                val selectItem = outProduList[position]
                val item1 = selectItem.produName
                val item2 = selectItem.produColor
                val item3 = selectItem.outAmount
                val item4 = selectItem.produMenu
                val item5 = selectItem.typeName

                //@@@@@@@单项删除
                testDel.setOnClickListener {
                    db.delete("nativeOutTable","lineName==? and produName==?  and produColor==?  and outAmount==? and produMenu==?",
                    arrayOf(item5,item1,item2,item3,item4) )
                    outProduList.remove(selectItem)
                    adapter.notifyDataSetChanged()
                }
            }
                //全部删除
                delOkButton.setOnClickListener {
                    db.delete("nativeOutTable", null,null)
                    outProduList.clear()
                    adapter.notifyDataSetChanged()
                }
            do {
                val type = cursor.getString(cursor.getColumnIndex("lineName"))
                val name = cursor.getString(cursor.getColumnIndex("produName"))
                val color = cursor.getString(cursor.getColumnIndex("produColor"))
                val amount = cursor.getInt(cursor.getColumnIndex("outAmount")).toString()
                val menu = cursor.getString(cursor.getColumnIndex("produMenu"))
                outProduList.add(OutProdu(type, name, color, amount, menu))

            }while (cursor.moveToNext())
        }
        cursor.close()
    }

    //初始化
    private fun initZhusuName(){
        val typeList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusu",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val typeName = cursor.getString(cursor.getColumnIndex("produName"))
                typeList.add(typeName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        outNameSearch.setAdapter(adapter)
    }
    //初始化非瓶坯注塑产品名
    private fun initOtherZhusuName(){
        val typeList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusuOther",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val typeName = cursor.getString(cursor.getColumnIndex("produName"))
                typeList.add(typeName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        outNameSearch.setAdapter(adapter)
    }
    private fun initChuisuName(){
        val typeList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeChuisu",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val typeName = cursor.getString(cursor.getColumnIndex("produName"))
                typeList.add(typeName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        outNameSearch.setAdapter(adapter)
    }
    private fun initJichuName(){
        val typeList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeJichu",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val typeName = cursor.getString(cursor.getColumnIndex("produName"))
                typeList.add(typeName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        outNameSearch.setAdapter(adapter)
    }
    private fun initOtherName(){
        val typeList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeOther",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val typeName = cursor.getString(cursor.getColumnIndex("produName"))
                typeList.add(typeName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        outNameSearch.setAdapter(adapter)
    }

    //初始化吹塑用瓶坯选择
    private fun initPingpiName(){
        val typeList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusu",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val typeName = cursor.getString(cursor.getColumnIndex("produName"))
                typeList.add(typeName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        outPingpiSearch.setAdapter(adapter)
    }

    //初始化颜色名称
    private fun initColorName(){
        val typeList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeProduColor",null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                val typeName = cursor.getString(cursor.getColumnIndex("produColor"))
                typeList.add(typeName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        outColorSearch.setAdapter(adapter)
    }


    private fun getUser(): String? {
        val userList = ArrayList<String>()
        val db = dbHelper.writableDatabase
        val cursor1 = db.query("nativeUser", null,null,null,null,null,null)
        if (cursor1.moveToFirst()){
            do {
                val userName = cursor1.getString(cursor1.getColumnIndex("userName"))
                userList.add(userName)
            }while (cursor1.moveToNext())
        }
        cursor1.close()

        return userList[0]
    }

    @SuppressLint("SimpleDateFormat")
    fun Date.getNowDate(): String {
        val sdf = SimpleDateFormat("yyyy-M-d")
        return sdf.format(this)
    }
}