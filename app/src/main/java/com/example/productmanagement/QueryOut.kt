package com.example.productmanagement

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
    val queryOutList = ArrayList<QueryProdu>()
    val queryChuisuOutList = ArrayList<QueryChuisuProdu>()
    val updateList = 1
    val handler1 = object : Handler(){
        override fun handleMessage(msg1: Message) {
            when (msg1.what){
                updateList -> adaperA()

            }
        }
    }
    val handler2 = object : Handler(){
        override fun handleMessage(msg2: Message) {
            when (msg2.what){
                updateList -> adaperB()
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
                    if (queryType == "瓶坯注塑"){
                        queryOutpingpi()
                    }else if (queryType == "非瓶坯注塑"){
                        queryOutNopingpi()
                    }else if (queryType == "吹塑"){
                        queryOutchuisu()
                    }else if (queryType == "挤出"){
                        queryOutjichu()
                    }else if (queryType == "其他"){
                        queryOutother()
                    }
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
        selectStartDateOut.setOnClickListener {
            buttonFuncA(R.id.startDateOut)
        }
        //选择结束日期
        selectLastDateOut.setOnClickListener {
            buttonFuncB(R.id.lastDateOut)
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
       val adapterA = QueryProduAdapter(this, R.layout.query_produ_item, queryOutList)
        listQueryOutView.adapter = adapterA
    }
    private fun adaperB(){
        val adapterB = QueryChuisuAdapter(this,R.layout.query_chuisu_item, queryChuisuOutList)
        listQueryOutView.adapter = adapterB
    }
    //查询瓶坯入库日期及数量等
    private fun queryOutpingpi(){
        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = outProduName.text
        val queryColor = outColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
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
                            queryOutList.add(QueryProdu("$date", "$amount", "$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
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
                            queryOutList.add(QueryProdu("$date", "$amount", "$menu"))
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
    //查询非瓶坯入库数
    private fun queryOutNopingpi(){
        val queryOutList = ArrayList<QueryProdu>()
        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = outProduName.text
        val queryColor = outColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
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
                            queryOutList.add(QueryProdu("$date", "$amount", "$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
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
                            queryOutList.add(QueryProdu("$date", "$amount", "$menu"))
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
    private fun queryOutchuisu(){

        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = outProduName.text
        val queryColor = outColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
                    val sql = "SELECT * FROM chuisuouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi = '$date1'"
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
                            queryChuisuOutList.add(QueryChuisuProdu("$date", "$amount", "$weight","$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
                    val sql = "SELECT * FROM chuisuouttable where name = '$queryOutName' AND color = '$queryColor' ANd riqi >= '$date1' and riqi <= '$date2'"
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
                            queryChuisuOutList.add(QueryChuisuProdu("$date", "$amount", "$weight","$menu"))
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
    private fun queryOutjichu(){
        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = outProduName.text
        val queryColor = outColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
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
                            queryOutList.add(QueryProdu("$date", "$amount", "$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
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
                            queryOutList.add(QueryProdu("$date", "$amount", "$menu"))
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
    private fun queryOutother(){
        val date1 = startDateOut.text.toString()
        val date2 = lastDateOut.text.toString()
        val queryOutName = outProduName.text
        val queryColor = outColorName.text
        try {
            thread {
                val conn = DBUtil().conection()
                if (date1 == date2){
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
                            queryOutList.add(QueryProdu("$date", "$amount", "$menu"))
                            //Log.d("QueryIn", "浏览入库数据成功1")
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else if (date1 < date2){
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
                            queryOutList.add(QueryProdu("$date", "$amount", "$menu"))
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
        outTypeName.text = typeList[0]
        outTypeName.setOnClickListener {
            selector("选择工序名称", typeList){i ->
                outTypeName.text = typeList[i]
                val queryType = outTypeName.text
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
        outProduName.text = typeList[0]
        outProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outProduName.text = typeList[i]
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
        outProduName.text = typeList[0]
        outProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outProduName.text = typeList[i]
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
        outProduName.text = typeList[0]
        outProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outProduName.text = typeList[i]
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
        outProduName.text = typeList[0]
        outProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outProduName.text = typeList[i]
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
        outProduName.text = typeList[0]
        outProduName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outProduName.text = typeList[i]
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
        outColorName.text = typeList[0]
        outColorName.setOnClickListener {
            selector("选择注塑产品名称", typeList){i ->
                outColorName.text = typeList[i]
            }
        }
    }
    //日期选择器
    private fun buttonFuncA(view:Int) {

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
    private fun buttonFuncB(view:Int) {

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