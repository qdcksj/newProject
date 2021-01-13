package com.example.productmanagement

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.in_bound_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import org.jetbrains.anko.selector
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

@Suppress("DEPRECATION")
class InBound : BaseActivity() {
    val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.in_bound_layout)
        setSupportActionBar(toolbar)
        initColorName()
        initTypeName()
        date_text1.text = Date().getNowDate()

        reFresh.setOnClickListener {
            freshInBound()
        }
        addNewProdu.setOnClickListener {
            val intent = Intent(this,AddProduName::class.java)
            startActivity(intent)
        }
        addProduColor.setOnClickListener {
            val intent = Intent(this, AddColorName::class.java)
            startActivity(intent)
        }

    }

    //初始化工序名称
    @SuppressLint("ResourceType", "SimpleDateFormat")
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
        lineSpiner.text = typeList[0]
        lineSpiner.setOnClickListener {
            selector("选择工序名称", typeList){i ->
                lineSpiner.text = typeList[i]

                when (lineSpiner.text) {
                    "吹塑" -> {
                        initChuisuName()
                        initPingpiName()
                    }
                    "瓶坯注塑" -> {
                        initZhusuName()
                        pingPiName1.text = "不使用瓶坯"
                    }
                    "非瓶坯注塑" -> {
                        initOtherZhusuName()
                        pingPiName1.text = "不使用瓶坯"
                    }
                    "挤出" -> {
                        pingPiName1.text = "不使用瓶坯"
                        initJichuName()
                    }
                    "其他" -> {
                        initOtherName()
                    }
                }

                dataSave.setOnClickListener {

                    when (lineSpiner.text) {
                        "吹塑" -> {
                            chusuInBound()
                            chusuAndPingpiOut()
                            "吹塑产品保存成功".showToast()
                        }
                        "瓶坯注塑" -> {
                            zhusuInBound()
                            "注塑瓶坯保存成功".showToast()
                        }
                        "非瓶坯注塑" -> {
                            zhusuOtherInBound()
                            "其他注塑产品保存成功".showToast()
                        }
                        "挤出" -> {
                            jichuInBound()
                            "挤出产品保存成功".showToast()
                        }
                        "其他" -> {
                            otherInBound()
                            "其他产品保存成功".showToast()
                        }
                    }
                }
            }
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

    //瓶坯注塑产品入库
    private fun zhusuInBound(){
        val produName = spiner11.text
        val weight = produWeightBt.text.toString()
        val colorName = spiner2.text
        val amount = editProduAmount.text.toString()
        val date1 = Date().getNowDate()
        val menu = editProduMenu.text.toString()
        val user1 = getUser()
        try {
            thread {
                val conn = DBUtil().conection()
                if (weight.isNotEmpty()){
                    if (amount.isNotEmpty()){
                        if (menu.isNotEmpty()){
                            try {
                                val sql = "insert into zhusuintable values(null, '$produName','$weight', '$colorName', '$amount', '$date1', '$menu', '$user1') "
                                // 创建用来执行sql语句的对象
                                val prepstat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num1 = prepstat.executeUpdate()
                                if (num1 > 0) {
                                    Log.d("InBound", "已添加到远程瓶坯注塑库")
                                } else {
                                    Log.d("InBound", "未添加到远程瓶坯注塑库")
                                }
                            } catch (e: SQLException) {
                            Log.e("InBound", "远程瓶坯注塑名称插入失败")
                            }
                        }else{
                            try {
                                val sql = "insert into zhusuintable  values(null ,'$produName','$weight', '$colorName', '$amount', '$date1', '无', '$user1') "
                                // 创建用来执行sql语句的对象
                                val stat = conn!!.prepareStatement(sql)
                                 // 执行sql查询语句并获取查询信息
                                val num2 = stat.executeUpdate()
                                if (num2 > 0) {
                                    Log.d("InBound", "已添加到远程瓶坯注塑库")
                                } else {
                                    Log.d("InBound", "未添加到远程瓶坯注塑库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程瓶坯注塑数据插入失败1")
                            }
                        }

                    }else{
                       Log.d("InBound", "数量不能为空")
                    }
                }else{
                    Log.d("InBound", "重量不能为空")
                }
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("InBound", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("InBound", "关闭连接失败。")
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    //非瓶坯注塑产品入库
    private fun zhusuOtherInBound(){

        val produName = spiner11.text
        val weight = produWeightBt.text
        val colorName = spiner2.text
        val amount = editProduAmount.text
        val date1 = Date().getNowDate()
        val menu = editProduMenu.text
        val user1 = getUser()
        try {
            thread {
                val conn = DBUtil().conection()
                if (weight.isNotEmpty()){
                    if (amount.isNotEmpty()){
                        if (menu.isNotEmpty()){
                            try {
                                val sql = "insert into zhusuotherintable values(null, '$produName','$weight', '$colorName', '$amount', '$date1', '$menu', '$user1') "
                                // 创建用来执行sql语句的对象
                                val prepstat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num1 = prepstat.executeUpdate()
                                if (num1 > 0) {
                                    Log.d("InBound", "已添加到远程非瓶坯注塑库")
                                } else {
                                    Log.d("InBound", "已添加到远程非瓶坯注塑库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程瓶非坯注塑名称插入失败")
                            }
                        }else{
                            try {
                                val sql = "insert into zhusuotherintable  values(null ,'$produName','$weight', '$colorName', '$amount', '$date1', '无', '$user1') "
                                // 创建用来执行sql语句的对象
                                val stat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num2 = stat.executeUpdate()
                                if (num2 > 0) {
                                    Log.d("InBound", "已添加到远程非瓶坯注塑库")
                                } else {
                                    Log.d("InBound", "未添加到远程非瓶坯注塑库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程非瓶坯注塑数据插入失败1")
                            }
                        }

                    }else{
                        Log.d("InBound", "数量不能为空")
                    }
                }else{
                    Log.d("InBound", "重量不能为空")
                }
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("InBound", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("InBound", "关闭连接失败。")
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    //吹塑产品入库
    private fun chusuInBound(){
        val pingpiName = pingPiName1.text
        val produName = spiner11.text
        val colorName = spiner2.text
        val amount = editProduAmount.text
        val date1 = Date().getNowDate()
        val menu = editProduMenu.text
        val user1 = getUser()
        try {
            thread {
                val conn = DBUtil().conection()
                if (amount.isNotEmpty()){
                    if (menu.isNotEmpty()){
                            try {
                                val sql = "insert into zhusuintable values(null, '$produName','$pingpiName', '$colorName', '$amount', '$date1', '$menu', '$user1') "
                                // 创建用来执行sql语句的对象
                                val prepstat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num1 = prepstat.executeUpdate()
                                if (num1 > 0) {
                                    Log.d("InBound", "已添加到远程吹塑库")
                                } else {
                                    Log.d("InBound", "未添加到远程吹塑库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程吹塑数据插入失败")
                            }
                    }else{
                        try {
                            val sql = "insert into zhusuintable  values(null ,'$produName','$pingpiName', '$colorName', '$amount', '$date1', '无', '$user1') "
                            // 创建用来执行sql语句的对象
                            val stat = conn!!.prepareStatement(sql)
                            // 执行sql查询语句并获取查询信息
                            val num2 = stat.executeUpdate()
                            if (num2 > 0) {
                                Log.d("InBound", "已添加到远程吹塑库")
                            } else {
                                Log.d("InBound", "未添加到远程吹塑库")
                            }
                        } catch (e: SQLException) {
                            Log.e("InBound", "远程吹塑数据插入失败1")
                        }
                    }
                }else{
                    Log.d("InBound", "数量量不能为空")
                }
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("InBound", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("InBound", "关闭连接失败。")
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    //吹塑相关的瓶坯出库记录操作
    private fun chusuAndPingpiOut(){
        val pingpiName = pingPiName1.text
        val colorName = spiner2.text
        val amount = editProduAmount.text
        val date1 = Date().getNowDate()
        val menu = editProduMenu.text
        val user1 = getUser()
        try {
            thread {
                val conn = DBUtil().conection()
                if (amount.isNotEmpty()){
                    if (menu.isNotEmpty()){

                            try {
                                val sql = "insert into zhusuouttable values(null, '$pingpiName', '$colorName', '$amount', '$date1', '$menu', '$user1') "
                                // 创建用来执行sql语句的对象
                                val prepstat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num1 = prepstat.executeUpdate()
                                if (num1 > 0) {
                                    Log.d("InBound", "已添加到远程瓶坯出库")
                                } else {
                                    Log.d("InBound", "未添加到远程瓶坯出库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程瓶坯出库插入失败")
                            }


                    }else{
                        try {
                            val sql = "insert into zhusuouttable  values(null ,'$pingpiName', '$colorName', '$amount', '$date1', '无', '$user1') "
                            // 创建用来执行sql语句的对象
                            val stat = conn!!.prepareStatement(sql)
                            // 执行sql查询语句并获取查询信息
                            val num2 = stat.executeUpdate()
                            if (num2 > 0) {
                                Log.d("InBound", "已添加到远程瓶坯出库")
                            } else {
                                Log.d("InBound", "未添加到远程瓶坯出库")
                            }
                        } catch (e: SQLException) {
                            Log.e("InBound", "远程瓶坯出库插入失败")
                        }
                    }
                }else{
                    Log.d("InBound", "数量不能为空")

                }
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("InBound", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("InBound", "关闭连接失败。")
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    //挤出产品入库
    private fun jichuInBound(){

        val produName = spiner11.text
        val weight = produWeightBt.text
        val colorName = spiner2.text
        val amount = editProduAmount.text
        val date1 = Date().getNowDate()
        val menu = editProduMenu.text
        val user1 = getUser()
        try {
            thread {
                val conn = DBUtil().conection()
                if (weight.isNotEmpty()){
                    if (amount.isNotEmpty()){
                        if (menu.isNotEmpty()){
                            try {
                                val sql = "insert into jichuintable values(null, '$produName','$weight', '$colorName', '$amount', '$date1', '$menu', '$user1') "
                                // 创建用来执行sql语句的对象
                                val prepstat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num1 = prepstat.executeUpdate()
                                if (num1 > 0) {
                                    Log.d("InBound", "已添加到远程瓶坯注塑库")
                                } else {
                                    Log.d("InBound", "未添加到远程瓶坯注塑库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程瓶坯注塑名称插入失败")
                            }
                        }else{
                            try {
                                val sql = "insert into jichuintable  values(null ,'$produName','$weight', '$colorName', '$amount', '$date1', '无', '$user1') "
                                // 创建用来执行sql语句的对象
                                val stat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num2 = stat.executeUpdate()
                                if (num2 > 0) {
                                    Log.d("InBound", "已添加到远程挤出库")
                                } else {
                                    Log.d("InBound", "未添加到远程挤出库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程挤出数据插入失败1")
                            }
                        }

                    }else{
                        Log.d("InBound", "数量不能为空")
                    }
                }else{
                    Log.d("InBound", "重量不能为空")
                }
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("InBound", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("InBound", "关闭连接失败。")
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    //其他产品入库
    private fun otherInBound(){
        val produName = spiner11.text
        val weight = produWeightBt.text
        val colorName = spiner2.text
        val amount = editProduAmount.text
        val date1 = Date().getNowDate()
        val menu = editProduMenu.text
        val user1 = getUser()
        try {
            thread {
                val conn = DBUtil().conection()
                if (weight.isNotEmpty()){
                    if (amount.isNotEmpty()){
                        if (menu.isNotEmpty()){
                            try {
                                val sql = "insert into zhusuintable values(null, '$produName','$weight', '$colorName', '$amount', '$date1', '$menu', '$user1') "
                                // 创建用来执行sql语句的对象
                                val prepstat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num1 = prepstat.executeUpdate()
                                if (num1 > 0) {
                                    Log.d("InBound", "已添加到远程其他库")
                                } else {
                                    Log.d("InBound", "未添加到远程其他库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程其他数据插入失败")
                            }
                        }else{
                            try {
                                val sql = "insert into zhusuintable  values(null ,'$produName','$weight', '$colorName', '$amount', '$date1', '无', '$user1') "
                                // 创建用来执行sql语句的对象
                                val stat = conn!!.prepareStatement(sql)
                                // 执行sql查询语句并获取查询信息
                                val num2 = stat.executeUpdate()
                                if (num2 > 0) {
                                    Log.d("InBound", "已添加到远程其他库")
                                } else {
                                    Log.d("InBound", "未添加到远程其他库")
                                }
                            } catch (e: SQLException) {
                                Log.e("InBound", "远程其他数据插入失败1")
                            }
                        }

                    }else{
                        Log.d("InBound", "数量不能为空")
                    }
                }else{
                    Log.d("InBound", "重量不能为空")
                }
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("InBound", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("InBound", "关闭连接失败。")
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    //刷新当前页面
    private fun freshInBound(){
        initColorName()
        initTypeName()

    }


    //初始化瓶坯产品名
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
        spiner11.text = typeList[0]
        spiner11.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                spiner11.text = typeList[i]
            }
        }
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
        spiner11.text = typeList[0]
        spiner11.setOnClickListener {
            selector("选择吹塑产品名称", typeList){i ->
                spiner11.text = typeList[i]
            }
        }
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
        spiner11.text = typeList[0]
        spiner11.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                spiner11.text = typeList[i]
            }
        }
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
        spiner11.text = typeList[0]
        spiner11.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                spiner11.text = typeList[i]
            }
        }
    }

    //初始化颜色名
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
        spiner2.text = typeList[0]
        spiner2.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                spiner2.text = typeList[i]
            }
        }
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
        spiner11.text = typeList[0]
        spiner11.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                spiner11.text = typeList[i]
            }
        }
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
        pingPiName1.text = typeList[0]
        pingPiName1.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                pingPiName1.text = typeList[i]
            }
        }
    }
    //获取操作者名称
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
    //获取日期
    @SuppressLint("SimpleDateFormat")
    fun Date.getNowDate(): String {
        val sdf = SimpleDateFormat("yyyy-M-d")
        return sdf.format(this)
    }
}