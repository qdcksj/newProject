package com.example.productmanagement


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.in_bound_layout.*
import kotlinx.android.synthetic.main.query_in_out_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import org.jetbrains.anko.selector
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import kotlin.concurrent.thread

class QueryInOut : BaseActivity() {
    val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.query_in_out_layout)
        setSupportActionBar(toolbar)
        initTypeSpinner()
        initColorSpinner()
        //initNameSpinner()

        outInQueryBtm.setOnClickListener {
            titleAll()
            titleBtm.setOnClickListener {
                val sumIn = queryInTitle.text.toString()
                val sumOut = queryOutTitle.text.toString()
                if (sumIn !== null){
                    println("库存数1：" + "$sumIn")
                    if (sumOut !== null){
                        println("库存数2：" + "$sumOut")
                        val sumAll = sumIn.toInt() - sumOut.toInt()
                        queryAllTitle.text = sumAll.toString()
                    }else{
                        println("无出库数据")
                    }
                }else {
                    println("无入库数据")
                }
            }
        }
        finishButton.setOnClickListener {
            ActivityCollector.finishAll()
        }

    }

    //依据工序选择出入库统计程序，并计算库存
    private fun titleAll(){
        val queryInOutType = outInTypeName.text
        if (queryInOutType == "瓶坯注塑"){
                zhusuInOut()
        }else if (queryInOutType == "非瓶坯注塑"){
            otherZhusuInOut()

        }else if (queryInOutType == "吹塑"){
            chuisuInOut()
        }else if (queryInOutType == "挤出"){
            jichuInOut()
        }else if (queryInOutType == "其他"){
            otherInOut()
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

    //瓶坯出入库统计
    private fun zhusuInOut() {
        val produName = outInProduName.text.toString()
        val produColor = outInColorName.text.toString()
        val queryInList1 = ArrayList<Int>()
        val queryOutList2 = ArrayList<Int>()
        try {
            thread {
                val conn = DBUtil().conection()
                try {
                    val sql1 = "SELECT * FROM zhusuintable where name ='$produName' and color ='$produColor'"
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    //val preparedStatement = conn!!.prepareStatement(sql1)
                    // 执行sql查询语句并获取查询信息
                    val rSet1: ResultSet = statement.executeQuery(sql1)
                    //val resultSet = preparedStatement.executeQuery()
                    //Log.d("QueryInout","查注塑1成功")
                    while (rSet1.next()){
                        val tile1 = rSet1.getString("shuliang").toInt()
                        queryInList1.add(tile1)
                        var sum1 = 0
                        for (a in queryInList1){
                            sum1 = sum1 + a
                            queryInTitle.text = sum1.toString()
                           // println("入库总数为："+sum1)
                        }
                       // Log.d("QueryInout","注塑数据加入集合成功")
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
            thread {
                val conn = DBUtil().conection()
                try {
                    val sql2 ="SELECT * FROM zhusuouttable where name = '$produName' and color = '$produColor'"
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    //val preparedStatement = conn!!.prepareStatement(sql2)
                    // 执行sql查询语句并获取查询信息
                    val rSet1: ResultSet = statement.executeQuery(sql2)
                    //val resultSet = preparedStatement.executeQuery()
                    Log.d("QueryInout", "查注塑2成功")
                    while (rSet1.next()) {
                        val title2 = rSet1.getString("shuliang").toInt()
                        queryOutList2.add(title2)
                        var sum2 = 0
                        for (b in queryOutList2){
                            sum2 = sum2 + b
                            queryOutTitle.text = sum2.toString()
                            println("出库总数为："+sum2)
                        }
                        Log.d("QueryInout","注塑数据加入集合2成功")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //非瓶坯出入库统计
    private fun otherZhusuInOut(){
        val produName = outInProduName.text.toString()
        val produColor = outInColorName.text.toString()
        val queryInList = ArrayList<Int>()
        val queryOutList = ArrayList<Int>()
        try {
            thread {
                val conn = DBUtil().conection()
                val sql1 = "SELECT * FROM zhusuotherintable where name = $produName AND color = $produColor"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet1: ResultSet = statement.executeQuery(sql1)
                    while (rSet1.next()){
                        val tile1 = rSet1.getInt("amount")
                        queryInList.add(tile1)
                        var sum1 = 0
                        for (a in queryInList){
                            sum1 = sum1 + a
                        }
                        queryInTitle.text = sum1.toString()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
            thread {
                val conn = DBUtil().conection()
                val sql2 = "SELECT * FROM zhusuotherouttable where name = $produName AND color = $produColor"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet2: ResultSet = statement.executeQuery(sql2)
                    while (rSet2.next()){
                        val title2 = rSet2.getInt("amount")
                        queryOutList.add(title2)
                        var sum2 = 0
                        for (b in queryOutList){
                            sum2 = sum2 + b
                        }
                        queryOutTitle.text = sum2.toString()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //吹塑出入库统计
    private fun chuisuInOut(){
        val produName = outInProduName.text.toString()
        val produColor = outInColorName.text.toString()
        val queryInList = ArrayList<Int>()
        val queryOutList = ArrayList<Int>()
        try {
            thread {
                val conn = DBUtil().conection()
                val sql1 = "SELECT * FROM chuisuintable where name = $produName AND color = $produColor"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet1: ResultSet = statement.executeQuery(sql1)
                    while (rSet1.next()){
                        val tile1 = rSet1.getInt("amount")
                        queryInList.add(tile1)
                        var sum1 = 0
                        for (a in queryInList){
                            sum1 = sum1 + a
                        }
                        queryInTitle.text = sum1.toString()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
            thread {
                val conn = DBUtil().conection()
                val sql2 = "SELECT * FROM chuisuouttable where name = $produName AND color = $produColor"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet2: ResultSet = statement.executeQuery(sql2)
                    while (rSet2.next()){
                        val title2 = rSet2.getInt("amount")
                        queryOutList.add(title2)
                        var sum2 = 0
                        for (b in queryOutList){
                            sum2 = sum2 + b
                        }
                        queryOutTitle.text = sum2.toString()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //挤出出入库统计
    private fun jichuInOut(){
        val produName = outInProduName.text.toString()
        val produColor = outInColorName.text.toString()
        val queryInList = ArrayList<Int>()
        val queryOutList = ArrayList<Int>()
        try {
            thread {
                val conn = DBUtil().conection()
                val sql1 = "SELECT * FROM jichuintable where name = $produName AND color = $produColor"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet1: ResultSet = statement.executeQuery(sql1)
                    while (rSet1.next()){
                        val tile1 = rSet1.getInt("amount")
                        queryInList.add(tile1)
                        var sum1 = 0
                        for (a in queryInList){
                            sum1 = sum1 + a
                        }
                        queryInTitle.text = sum1.toString()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
            thread {
                val conn = DBUtil().conection()
                val sql2 = "SELECT * FROM jichuouttable where name = $produName AND color = $produColor"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet2: ResultSet = statement.executeQuery(sql2)
                    while (rSet2.next()){
                        val title2 = rSet2.getInt("amount")
                        queryOutList.add(title2)
                        var sum2 = 0
                        for (b in queryOutList){
                            sum2 = sum2 + b
                        }
                        queryOutTitle.text = sum2.toString()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //其他出入库统计
    private fun otherInOut(){
        val produName = outInProduName.text.toString()
        val produColor = outInColorName.text.toString()
        val queryInList = ArrayList<Int>()
        val queryOutList = ArrayList<Int>()
        try {
            thread {
                val conn = DBUtil().conection()
                val sql1 = "SELECT * FROM otherintable where name = $produName AND color = $produColor"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet1: ResultSet = statement.executeQuery(sql1)
                    while (rSet1.next()){
                        val tile1 = rSet1.getInt("amount")
                        queryInList.add(tile1)
                        var sum1 = 0
                        for (a in queryInList){
                            sum1 = sum1 + a
                        }
                        queryInTitle.text = sum1.toString()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
            thread {
                val conn = DBUtil().conection()
                val sql2 = "SELECT * FROM otherouttable where name = $produName AND color = $produColor"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet2: ResultSet = statement.executeQuery(sql2)
                    while (rSet2.next()){
                        val title2 = rSet2.getInt("amount")
                        queryOutList.add(title2)
                        var sum2 = 0
                        for (b in queryOutList){
                            sum2 = sum2 + b
                        }
                        queryOutTitle.text = sum2.toString()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //初始化各Spinner

    private fun initNameSpinner(){
        val queryInOutType = outInTypeName.text
        if (queryInOutType == "瓶坯注塑"){
            initZhusuSpinner()
        }else if (queryInOutType == "非瓶坯注塑"){
            initOtherZhusuSpinner()
        }else if (queryInOutType == "吹塑"){
            initChuisuSpinner()
        }else if (queryInOutType == "挤出"){
            initJichuSpinner()
        }else if (queryInOutType == "其他"){
            initOtherSpinner()
        }
    }

    //初始化工序名称
    private fun initTypeSpinner(){
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
        outInTypeName.text = typeList[0]
        outInTypeName.setOnClickListener {
            selector("选择工序名称", typeList){i ->
                outInTypeName.text = typeList[i]
                val queryInOutType = outInTypeName.text
                if (queryInOutType == "瓶坯注塑"){
                    initZhusuSpinner()
                }else if (queryInOutType == "非瓶坯注塑"){
                    initOtherZhusuSpinner()
                }else if (queryInOutType == "吹塑"){
                    initChuisuSpinner()
                }else if (queryInOutType == "挤出"){
                    initJichuSpinner()
                }else if (queryInOutType == "其他"){
                    initOtherSpinner()
                }
            }
        }
    }

    //初始化瓶坯产品名
    private fun initZhusuSpinner(){
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
        outInProduName.text = typeList[0]
        outInProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outInProduName.text = typeList[i]
            }
        }
    }
    //初始化非瓶坯注塑产品名
    private fun initOtherZhusuSpinner(){
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
        outInProduName.text = typeList[0]
        outInProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outInProduName.text = typeList[i]
            }
        }
    }
    private fun initChuisuSpinner(){
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
        outInProduName.text = typeList[0]
        outInProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outInProduName.text = typeList[i]
            }
        }
    }
    private fun initJichuSpinner(){
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
        outInProduName.text = typeList[0]
        outInProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outInProduName.text = typeList[i]
            }
        }
    }
    private fun initOtherSpinner(){
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
        outInProduName.text = typeList[0]
        outInProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outInProduName.text = typeList[i]
            }
        }
    }

    //初始化颜色名
    private fun initColorSpinner(){
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
        outInColorName.text = typeList[0]
        outInColorName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outInColorName.text = typeList[i]
            }
        }
    }


}