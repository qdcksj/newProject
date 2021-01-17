package com.example.productmanagement

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.productmanagement.myclass.SearchAdapter
import kotlinx.android.synthetic.main.query_in_layout.*
import kotlinx.android.synthetic.main.query_out_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.selector
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

@Suppress("DEPRECATION")
class QueryOut : BaseActivity()  {
    val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
    private val queryChuisuOutList = ArrayList<QueryChuisuProdu>()

    val updateList = 1
    private val handler1 = @SuppressLint("HandlerLeak")
    object : Handler(){
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg1: Message) {
            when (msg1.what){
                updateList -> adaperA()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.query_out_layout)
        setSupportActionBar(toolbar)
        initTypeSpinner()
        initColorSpinner()

        //点击查询事件
        outQueryBtm.setOnClickListener {
            val job = Job()
            val scope = CoroutineScope(job)
            val queryType = outTypeName.text
            thread {
                scope.launch {
                    when (queryType) {
                        "瓶坯注塑" -> {
                            queryOutpingpi()
                        }
                        "非瓶坯注塑" -> {
                            queryOutNopingpi()
                        }
                        "吹塑" -> {
                            queryOutchuisu()
                        }
                        "挤出" -> {
                            queryOutjichu()
                        }
                        "其他" -> {
                            queryOutother()
                        }
                    }
                }
                scope.launch {
                        val msg1 = Message()
                        msg1.what = updateList
                        delay(1000)
                        handler1.sendMessage(msg1)
                        //println("handler运行")

                }
                job.cancel()
            }
        }
        reFreshQueryBtm.setOnClickListener {
            reFresh()
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
    private fun adaperA(){
       val adapterA = QueryChuisuAdapter(this, R.layout.query_chuisu_item, queryChuisuOutList)
        listQueryOutView.adapter = adapterA
    }

    //刷新当前页面
    private fun reFresh(){
        initTypeSpinner()
        initColorSpinner()

        queryChuisuOutList.clear()
        val adapterA = QueryChuisuAdapter(this, R.layout.query_chuisu_item, queryChuisuOutList)
        listQueryOutView.adapter = adapterA
    }

    //查询瓶坯入库日期及数量等
    private fun queryOutpingpi(){
        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = queryOutNameSearch.text.toString()
        val queryColor = queryOutColorSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM zhusuouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, "无",menu))
                                //Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM zhusuouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)

                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, "无",menu))
                                //Log.d("QueryIn", "浏览入库数据成功2")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                    else -> {
                        //Log.d("QueryIn", "截止日期在开始日期之前了")
                        Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
                    }
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
    //查询非瓶坯入库数
    private fun queryOutNopingpi(){
        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = queryOutNameSearch.text.toString()
        val queryColor = queryOutColorSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM zhusuotherouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, "无",menu))
                                //Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM zhusuotherouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)

                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, "无",menu))
                                //Log.d("QueryIn", "浏览入库数据成功2")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                    else -> {
                        //Log.d("QueryIn", "截止日期在开始日期之前了")
                        Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
                    }
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
    //查询吹塑入库数
    private fun queryOutchuisu(){

        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = queryOutNameSearch.text.toString()
        val queryColor = queryOutColorSearch.text.toString()
        val queryPingpi = queryOutPingpiSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM chuisuouttable where name = '$queryOutName' AND pingpiname = '$queryPingpi' and color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                val pingpi = rSet.getString("pingpiname")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, pingpi, menu))
                                //Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM chuisuouttable where name = '$queryOutName' AND pingpiname = '$queryPingpi' and color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                val pingpi = rSet.getString("pingpiname")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, pingpi, menu))
                                //Log.d("QueryIn", "浏览入库数据成功2")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    else -> {
                        //Log.d("QueryIn", "截止日期在开始日期之前了")
                        Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
                    }
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
    //查询挤出入库数
    private fun queryOutjichu(){
        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = queryOutNameSearch.text.toString()
        val queryColor = queryOutColorSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM jichuouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, "无",menu))
                                //Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM jichuouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)

                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, "无",menu))
                                //Log.d("QueryIn", "浏览入库数据成功2")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    else -> {
                        //Log.d("QueryIn", "截止日期在开始日期之前了")
                        Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
                    }
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
    //查询其他入库数
    private fun queryOutother(){
        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = queryOutNameSearch.text.toString()
        val queryColor = queryOutColorSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM otherouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, "无",menu))
                                //Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM zhusuouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)

                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuOutList.add(QueryChuisuProdu(date, amount, "无",menu))
                                //Log.d("QueryIn", "浏览入库数据成功2")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                    else -> {
                        //Log.d("QueryIn", "截止日期在开始日期之前了")
                        Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
                    }
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
        outTypeName.text = ""
        outTypeName.setOnClickListener {
            selector("选择工序名称", typeList){i ->
                outTypeName.text = typeList[i]
                when (outTypeName.text) {
                    "瓶坯注塑" -> {
                        initZhusuSpinner()
                        queryOutPingpiSearch.setText("不使用瓶坯")
                    }
                    "非瓶坯注塑" -> {
                        initOtherZhusuSpinner()
                        queryOutPingpiSearch.setText("不使用瓶坯")
                    }
                    "吹塑" -> {
                        initChuisuSpinner()
                        initPingpiName()
                    }
                    "挤出" -> {
                        initJichuSpinner()
                        queryOutPingpiSearch.setText("不使用瓶坯")
                    }
                    "其他" -> {
                        initOtherSpinner()
                        queryOutPingpiSearch.setText("不使用瓶坯")
                    }
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
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        queryOutNameSearch.setAdapter(adapter)
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
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        queryOutNameSearch.setAdapter(adapter)
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
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        queryOutNameSearch.setAdapter(adapter)
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
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        queryOutNameSearch.setAdapter(adapter)
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
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        queryOutNameSearch.setAdapter(adapter)
    }
    //初始化吹塑用瓶坯选择
    private fun initPingpiName(){
        val typeList = java.util.ArrayList<String>()
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
        queryOutPingpiSearch.setAdapter(adapter)
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
        val adapter = SearchAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, typeList)
        queryOutColorSearch.setAdapter(adapter)
    }
    //日期选择器
     fun buttonFunc3(view:View) {
        when(view.id){
            R.id.startDateOut ->{
                val ca = Calendar.getInstance()
                var mYear = ca[Calendar.YEAR]
                var mMonth = ca[Calendar.MONTH]
                var mDay = ca[Calendar.DAY_OF_MONTH]

                val datePickerDialog = DatePickerDialog(
                    this,
                    AlertDialog.THEME_HOLO_DARK,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        mYear = year
                        mMonth = month
                        mDay = dayOfMonth
                        val mDate = "${year}-${month + 1}-${dayOfMonth}"
                        // 将选择的日期赋值给TextView
                        startDate.text = mDate
                    },
                    mYear, mMonth, mDay
                )
                datePickerDialog.show()
            }
        }

    }
     fun buttonFunc4(view:View) {
        when(view.id){
            R.id.lastDateOut ->{
                val ca = Calendar.getInstance()
                var mYear = ca[Calendar.YEAR]
                var mMonth = ca[Calendar.MONTH]
                var mDay = ca[Calendar.DAY_OF_MONTH]

                val datePickerDialog = DatePickerDialog(
                    this,
                    AlertDialog.THEME_HOLO_DARK,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        mYear = year
                        mMonth = month
                        mDay = dayOfMonth
                        val mDate = "${year}-${month + 1}-${dayOfMonth}"
                        // 将选择的日期赋值给TextView
                        lastDate.text = mDate
                    },
                    mYear, mMonth, mDay
                )
                datePickerDialog.show()
            }
        }

    }

}