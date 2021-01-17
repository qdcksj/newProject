package com.example.productmanagement

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
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
class QueryIn : BaseActivity()  {
    val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
    private val queryChuisuList = ArrayList<QueryChuisuProdu>()
    val updateList = 1
    private val handler1 = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg1: Message) {
            when (msg1.what){
                updateList -> adaper1()
            }
        }
    }

            override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.query_in_layout)
         setSupportActionBar(toolbar)
         initTypeSpinner()
          initColorSpinner()

    //点击查询事件
    queryBtm.setOnClickListener {
            val job = Job()
            val scope = CoroutineScope(job)
        val queryType = inTypeName.text
        thread {
            scope.launch {
                when (queryType) {
                    "瓶坯注塑" -> {
                        queryInpingpi()
                    }
                    "非瓶坯注塑" -> {
                        queryInNopingpi()
                    }
                    "吹塑" -> {
                        queryInchuisu()
                    }
                    "挤出" -> {
                        queryInjichu()
                    }
                    "其他" -> {
                        queryInother()
                    }
                    //println("协程运行完")
                }
                //println("协程运行完")
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
                reFreshBtm.setOnClickListener {

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

    private fun adaper1(){
        val adapter1 = QueryChuisuAdapter(this, R.layout.query_chuisu_item, queryChuisuList)
        listQueryInView.adapter = adapter1
    }
    private fun reFresh() {
        initTypeSpinner()
        initColorSpinner()
        queryChuisuList.clear()
        val adapterA = QueryChuisuAdapter(this, R.layout.query_chuisu_item, queryChuisuList)
        listQueryInView.adapter = adapterA
    }
    //查询瓶坯入库日期及数量等
    private fun queryInpingpi() {
        val date1 = startDate.text.toString()
        val date2 = lastDate.text.toString()
        val queryInName = queryInNameSearch.text.toString()
        val queryColor = queryInColorSearch.text.toString()
        try {
            thread {
                    val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM zhusuintable where name = '$queryInName' AND color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuList.add(QueryChuisuProdu(date, amount,"无", menu))
                                Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM zhusuintable where name = '$queryInName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)

                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuList.add(QueryChuisuProdu(date, amount,"无", menu))
                                Log.d("QueryIn", "浏览入库数据成功2")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                    else -> {
                        Log.d("QueryIn", "截止日期在开始日期之前了")
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
    private fun queryInNopingpi(){

        val date1 = startDate.text.toString()
        val date2 = lastDate.text.toString()
        val queryInName = queryInNameSearch.text.toString()
        val queryColor = queryInColorSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM zhusuotherintable where name = '$queryInName' AND color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuList.add(QueryChuisuProdu(date, amount,"无", menu))
                                //Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM zhusuotherintable where name = '$queryInName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuList.add(QueryChuisuProdu(date, amount,"无", menu))
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
    private fun queryInchuisu(){
        val date1 = startDate.text.toString()
        val date2 = lastDate.text.toString()
        val queryInName = queryInNameSearch.text.toString()
        val queryColor = queryInColorSearch.text.toString()
        val queryPingpi = queryInPingpiSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM chuisuintable where name = '$queryInName' AND pingpiname = '$queryPingpi' and  color = '$queryColor' ANd riqi = '$date1'"
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
                                queryChuisuList.add(QueryChuisuProdu(date, amount, pingpi, menu))
                                Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM chuisuintable where name = '$queryInName' AND pingpiname = '$queryPingpi' and color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
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
                                queryChuisuList.add(QueryChuisuProdu(date, amount, pingpi, menu))
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
    private fun queryInjichu(){
        val date1 = startDate.text.toString()
        val date2 = lastDate.text.toString()
        val queryInName = queryInNameSearch.text.toString()
        val queryColor = queryInColorSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM jichuintable where name = '$queryInName' AND color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuList.add(QueryChuisuProdu(date, amount,"无", menu))
                                //Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM jichuintable where name = '$queryInName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)

                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuList.add(QueryChuisuProdu(date, amount,"无", menu))
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
    private fun queryInother(){
        val date1 = startDate.text.toString()
        val date2 = lastDate.text.toString()
        val queryInName = queryInNameSearch.text.toString()
        val queryColor = queryInColorSearch.text.toString()
        try {
            thread {
                val conn = DBUtil().conection()
                when {
                    date1 == date2 -> {
                        val sql = "SELECT * FROM otherintable where name = '$queryInName' AND color = '$queryColor' ANd riqi = '$date1'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)
                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuList.add(QueryChuisuProdu(date, amount,"无", menu))
                                Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    date1 < date2 -> {
                        val sql = "SELECT * FROM otherintable where name = '$queryInName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                        try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn!!.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val rSet: ResultSet = statement.executeQuery(sql)

                            while (rSet.next()){
                                val amount = rSet.getString("shuliang")
                                val menu = rSet.getString("menu")
                                val date = rSet.getString("riqi")
                                queryChuisuList.add(QueryChuisuProdu(date, amount,"无", menu))
                                Log.d("QueryIn", "浏览入库数据成功2")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                    else -> {
                        Log.d("QueryIn", "截止日期在开始日期之前了")
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
    inTypeName.text = ""
    inTypeName.setOnClickListener {
        selector("选择工序名称", typeList){i ->
            inTypeName.text = typeList[i]

            when (inTypeName.text) {
                "瓶坯注塑" -> {
                    initZhusuSpinner()
                    queryInPingpiSearch.setText("不使用瓶坯")
                }
                "非瓶坯注塑" -> {
                    initOtherZhusuSpinner()
                    queryInPingpiSearch.setText("不使用瓶坯")
                }
                "吹塑" -> {
                    initChuisuSpinner()
                    initPingpiName()
                }
                "挤出" -> {
                    initJichuSpinner()
                    queryInPingpiSearch.setText("不使用瓶坯")
                }
                "其他" -> {
                    initOtherSpinner()
                    queryInPingpiSearch.setText("不使用瓶坯")
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
    queryInNameSearch.setAdapter(adapter)
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
    queryInNameSearch.setAdapter(adapter)
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
    queryInNameSearch.setAdapter(adapter)
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
    queryInNameSearch.setAdapter(adapter)
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
    queryInNameSearch.setAdapter(adapter)
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
        queryInPingpiSearch.setAdapter(adapter)
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
    queryInColorSearch.setAdapter(adapter)
}



//日期选择器
 fun buttonFunc1(view:View) {
    when(view.id){
        R.id.startDate ->{
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
fun buttonFunc2(view:View) {
    when (view.id) {
        R.id.lastDate -> {
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



