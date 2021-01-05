package com.example.productmanagement

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.query_in_layout.*
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
    val queryInList = ArrayList<QueryProdu>()
    val queryChuisuList = ArrayList<QueryChuisuProdu>()
    val updateList = 1
    val handler1 = object : Handler(){
        override fun handleMessage(msg1: Message) {
            when (msg1.what){
                updateList -> adaper1()

            }
        }
    }
    val handler2 = object : Handler(){
        override fun handleMessage(msg2: Message) {
            when (msg2.what){
                updateList -> adaper2()
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
                if (queryType == "瓶坯注塑"){
                    queryInpingpi()
                }else if (queryType == "非瓶坯注塑"){
                    queryInNopingpi()
                }else if (queryType == "吹塑"){
                    queryInchuisu()
                }else if (queryType == "挤出"){
                    queryInjichu()
                }else if (queryType == "其他"){
                    queryInother()
                }
                //println("协程运行完")
            }
            scope.launch {
                if (queryType == "吹塑"){
                    val msg2 = Message()
                    msg2.what = updateList
                    delay(1000)
                    handler2.sendMessage(msg2)
                }else{
                    val msg1 = Message()
                    msg1.what = updateList
                    delay(1000)
                    handler1.sendMessage(msg1)
                    //println("handler运行")
                }
            }
            job.cancel()
        }
    }

    //选择开始日期
    selectStartDate.setOnClickListener {
        buttonFunc1(R.id.startDate)
    }
    //选择结束日期
    selectLastDate.setOnClickListener {
        buttonFunc2(R.id.lastDate)
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
        val adapter1 = QueryProduAdapter(this, R.layout.query_produ_item, queryInList)
        listQueryInView.adapter = adapter1
    }
    private fun adaper2(){
        val adapter2 = QueryChuisuAdapter(this, R.layout.query_chuisu_item, queryChuisuList)
        listQueryInView.adapter = adapter2
    }

    //查询瓶坯入库日期及数量等
    private fun queryInpingpi() {
        val date1 = startDate.text.toString()
        val date2 = lastDate.text.toString()
        val queryInName = inProduName.text.toString()
        val queryColor = inColorName.text.toString()
        try {
            thread {
                    val conn = DBUtil().conection()
                    if (date1 == date2){
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
                                queryInList.add(QueryProdu("$date", "$amount", "$menu"))
                                Log.d("QueryIn", "浏览入库数据成功1")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }else if (date1 < date2){
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
                                queryInList.add(QueryProdu("$date", "$amount", "$menu"))
                                Log.d("QueryIn", "浏览入库数据成功2")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }else {
                        Log.d("QueryIn", "截止日期在开始日期之前了")
                        Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
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
        val queryInList = ArrayList<QueryProdu>()
        val date1 = startDate.text.toString()
        val date2 = lastDate.text.toString()
        val queryInName = inProduName.text
        val queryColor = inColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
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
                            queryInList.add(QueryProdu("$date", "$amount", "$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
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
                            queryInList.add(QueryProdu("$date", "$amount", "$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功2")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else {
                    //Log.d("QueryIn", "截止日期在开始日期之前了")
                    Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
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
        val queryInName = inProduName.text
        val queryColor = inColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
                    val sql = "SELECT * FROM chuisuintable where name = '$queryInName' AND color = '$queryColor' ANd riqi = '$date1'"
                    try {
                        // 创建用来执行sql语句的对象
                        val statement: Statement = conn!!.createStatement()
                        // 执行sql查询语句并获取查询信息
                        val rSet: ResultSet = statement.executeQuery(sql)
                        while (rSet.next()){
                            val amount = rSet.getString("shuliang")
                            val menu = rSet.getString("menu")
                            val date = rSet.getString("riqi")
                            val weight = rSet.getString("pingpiname")
                            queryChuisuList.add(QueryChuisuProdu("$date", "$amount", "$weight","$menu"))
                            Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
                    val sql = "SELECT * FROM chuisuintable where name = '$queryInName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
                    try {
                        // 创建用来执行sql语句的对象
                        val statement: Statement = conn!!.createStatement()
                        // 执行sql查询语句并获取查询信息
                        val rSet: ResultSet = statement.executeQuery(sql)
                        while (rSet.next()){
                            val amount = rSet.getString("shuliang")
                            val menu = rSet.getString("menu")
                            val date = rSet.getString("riqi")
                            val weight = rSet.getString("pingpiname")
                            queryChuisuList.add(QueryChuisuProdu("$date", "$amount", "$weight","$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功2")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else {
                    //Log.d("QueryIn", "截止日期在开始日期之前了")
                    Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
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
        val queryInName = inProduName.text
        val queryColor = inColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
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
                            queryInList.add(QueryProdu("$date", "$amount", "$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
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
                            queryInList.add(QueryProdu("$date", "$amount", "$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功2")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }else {
                    //Log.d("QueryIn", "截止日期在开始日期之前了")
                    Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
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
        val queryInName = inProduName.text
        val queryColor = inColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
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
                            queryInList.add(QueryProdu("$date", "$amount", "$menu"))
                            Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
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
                            queryInList.add(QueryProdu("$date", "$amount", "$menu"))
                            Log.d("QueryIn", "浏览入库数据成功2")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }else {
                    Log.d("QueryIn", "截止日期在开始日期之前了")
                    Toast.makeText(this, "截止日期不对", Toast.LENGTH_SHORT).show()
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
    inTypeName.text = typeList[0]
    inTypeName.setOnClickListener {
        selector("选择工序名称", typeList){i ->
            inTypeName.text = typeList[i]

            val queryType = inTypeName.text
            if (queryType == "瓶坯注塑"){
                initZhusuSpinner()
            }else if (queryType == "非瓶坯注塑"){
                initOtherZhusuSpinner()
            }else if (queryType == "吹塑"){
                initChuisuSpinner()
            }else if (queryType == "挤出"){
                initJichuSpinner()
            }else if (queryType == "其他"){
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
    inProduName.text = typeList[0]
    inProduName.setOnClickListener {
        selector("选择注塑产品名称", typeList){i ->
            inProduName.text = typeList[i]
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
    inProduName.text = typeList[0]
    inProduName.setOnClickListener {
        selector("选择注塑产品名称", typeList){i ->
            inProduName.text = typeList[i]
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
    inProduName.text = typeList[0]
    inProduName.setOnClickListener {
        selector("选择注塑产品名称", typeList){i ->
            inProduName.text = typeList[i]
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
    inProduName.text = typeList[0]
    inProduName.setOnClickListener {
        selector("选择注塑产品名称", typeList){i ->
            inProduName.text = typeList[i]
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
    inProduName.text = typeList[0]
    inProduName.setOnClickListener {
        selector("选择注塑产品名称", typeList){i ->
            inProduName.text = typeList[i]
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
    inColorName.text = typeList[0]
    inColorName.setOnClickListener {
        selector("选择注塑产品名称", typeList){i ->
            inColorName.text = typeList[i]
        }
    }
}



//日期选择器
private fun buttonFunc1(view:Int) {

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
private fun buttonFunc2(view:Int) {

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


