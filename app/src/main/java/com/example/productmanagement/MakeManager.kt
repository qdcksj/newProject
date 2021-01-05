package com.example.productmanagement

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.contentValuesOf
import kotlinx.android.synthetic.main.maker_manager_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MakeManager : BaseActivity() {
    val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maker_manager_layout)
        setSupportActionBar(toolbar)

        //创建本地数据库（出库临时显示库，本地品名库，本地颜色库）
        thread { CreatDatabase() }
        thread {  initNativeZhusu() }
        thread { initNativeZhusuOther() }
        thread { initNativeChuisu() }
        thread { initNativeJichu() }
        thread { initNativeOther() }
        thread { initNativeColor() }
        thread { initNativeType() } //初始化工序库

        //退出程序入口，注意，程序需要继承自BaseActivity()才可以使用本入口
        outMakeManager.setOnClickListener {
            ActivityCollector.finishAll()
        }
        //入库操作入口

        intoStoreBtm.setOnClickListener {
            thread {
                val intent = Intent(this,InBound::class.java)
                startActivity(intent)
            }
        }
        //出库操作入口
        outStoreBtm.setOnClickListener {
            thread {
                val intent = Intent(this,OutBound::class.java)
                startActivity(intent)
            }

        }
        //修改数据入口
        editDataBtm.setOnClickListener {
            thread {
                //val intent = Intent(this,UpDataManager::class.java)
                val intent = Intent(this, UpDataManager::class.java)
                startActivity(intent)
            }

        }
        //查询操作入口
        listDataBtm.setOnClickListener {
            thread {
                val intent = Intent(this,QueryManager::class.java)
                startActivity(intent)
            }
        }
    }
    //屏蔽返回键
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //这里写你要在用户按下返回键同时执行的动作
            moveTaskToBack(false)   //核心代码：屏蔽返回行为
            return true
        }
            return super.onKeyDown(keyCode, event)
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

    //创建本地数据库
    private fun CreatDatabase(){

        dbHelper.writableDatabase

        //Log.d("MakeManager", "本地数据库创建成功")
    }

    //初始化本地工序库
    private fun initNativeType(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeType",null,null,null,null,null,null)

        if (cursor.count == 0){
            //如果首次创建数据库，则将远程数据库全部导入
            upNativeType()
            //Log.d("MakeManager","本地数据库更新成功")
        }else{
            //Log.d("MakeManager","本地工序库无更新")
        }

    }
//初始化本地瓶坯注塑库
    @SuppressLint("Recycle")
    private fun initNativeZhusu(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusu",null,null,null,null,null,null)

        if (cursor.count == 0){
            //如果首次创建数据库，则将远程数据库全部导入
              upZhusuAllData()
            //Log.d("MakeManager","注塑数据库插入成功")
        }else{
            upZhusuData()
        }
     }
    //初始化本地非瓶坯注塑库
    @SuppressLint("Recycle")
    private fun initNativeZhusuOther(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeZhusuOther",null,null,null,null,null,null)

        if (cursor.count == 0){
            //如果首次创建数据库，则将远程数据库全部导入
            upZhusuOtherAllData()
            //Log.d("MakeManager","其他注塑数据库插入成功")
        }else{
            upZhusuOtherData()
        }
    }

    //初始化本地吹塑库
    @SuppressLint("Recycle")
    private fun initNativeChuisu(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeChuisu",null,null,null,null,null,null)

        if (cursor.count == 0){
            //如果首次创建数据库，则将远程数据库全部导入
            upChuisuAllData()
            //Log.d("MakeManager","吹塑数据库插入成功")
        }else{
            upChuisuData()
        }
    }

    ////初始化本地挤出库
    @SuppressLint("Recycle")
    private fun initNativeJichu(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeJichu",null,null,null,null,null,null)

        if (cursor.count == 0){
            //如果首次创建数据库，则将远程数据库全部导入
            upJichuAllData()
            //Log.d("MakeManager","挤出数据库插入成功")
        }else{
            upJichuData()
        }
    }

    //初始化本地其他库
    @SuppressLint("Recycle")
    private fun initNativeOther(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeOther",null,null,null,null,null,null)

        if (cursor.count == 0){
            //如果首次创建数据库，则将远程数据库全部导入
            upOtherAllData()
            //Log.d("MakeManager","其他数据库插入成功")
        }else{
            upOtherData()
        }
    }
    //初始化本地颜色库
    @SuppressLint("Recycle")
    private fun initNativeColor(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("nativeProduColor",null,null,null,null,null,null)

        if (cursor.count == 0){
            //如果首次创建数据库，则将远程数据库全部导入
            upColorAllData()
            //Log.d("MakeManager","颜色数据库插入成功")
        }else{
            upColorData()
        }
    }
//获取远程工序库数据
    private fun upNativeType(){
        val db = dbHelper.writableDatabase
        val typeList = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM worktype "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val typeName = rSet.getString("typename")
                        typeList.add(typeName)
                    }
                } catch (e: SQLException) {
                    //Log.e("MakeManager", "工序名称插入失败")
                }
                for (name in typeList){
                    val values = contentValuesOf("typeName" to name)
                    db.insert("nativeType", null, values)
                    //Log.d("MakeManager","工序名称插入成功")
                    //Log.d("MakeManager", "数据库内名称是：$name")
                }
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

//注塑本地库更新
    private fun upZhusuAllData(){
        val db = dbHelper.writableDatabase
        val zhusuList = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM zhusuprodunametable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameZhusu = rSet.getString("name")
                        zhusuList.add(nameZhusu)
                    }
                } catch (e: SQLException) {
                   // Log.e("MakeManager", "注塑名称插入失败")
                }
                for (name in zhusuList){
                    val values = contentValuesOf("produName" to name)
                    db.insert("nativeZhusu", null, values)
                   // Log.d("MakeManager","注塑名称插入成功")
                    //Log.d("MakeManager", "数据库内名称是：$name")
                }
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private fun upZhusuData(){
        val db = dbHelper.writableDatabase
        val zhusuList1 = ArrayList<String>()
        val zhusuList2 = ArrayList<String>()
        val cursor = db.query("nativeZhusu",null,null,null,null,null,null)
        val nativeList = ArrayList<String>()
        val nativeList1 = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM zhusuprodunametable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameZhusu = rSet.getString("name")
                        zhusuList1.add(nameZhusu)
                    }
                } catch (e: SQLException) {
                    //Log.e("MakeManager", "注塑名称搜索失败")
                }
                Log.d("MakeManager", "远程注塑库准备完成")
                if (cursor.moveToFirst()){
                    do {
                        val name1 = cursor.getString(cursor.getColumnIndex("produName"))
                        nativeList.add(name1)
                    }while (cursor.moveToNext())
                }
                cursor.close()
                //Log.d("MakeManager", "本地注塑库准备完成")
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                if (zhusuList1.count() == nativeList.count()){
                //判断需要更新本地还是远程库
                    nativeList.removeAll(zhusuList1)
                    zhusuList2.removeAll(nativeList1)
                    val count1 = nativeList.count()
                    val count2 = zhusuList2.count()
                    if (count1 > 0){
                        for (m in nativeList){
                            db.delete("nativeZhusu", "produName == ?", arrayOf(m))
                            //Log.d("MakeManager", "本地注塑库删除已更新")
                        }
                    }else if (count2 > 0){
                        for (n in zhusuList2){
                            val values = contentValuesOf("produName" to n)
                            db.insert("nativeZhusu", null, values)
                            //Log.d("MakeManager", "本地注塑库增加已更新")
                        }
                    }else{
                        //Log.d("MakeManager", "注塑数据库无更新")
                    }

                }else if (zhusuList1.count() > nativeList.count()){
                    zhusuList1.removeAll(nativeList)
                    for (i in zhusuList1){
                        val values = contentValuesOf("produName" to i)
                        db.insert("nativeZhusu", null, values)
                        //Log.d("MakeManager", "可更新的数据是：+$i+已更新至本地注塑库")
                    }
                }else{
                    nativeList.removeAll(zhusuList1)
                    for (i in nativeList)
                        db.delete("nativeZhusu", "produName == ?", arrayOf(i))
                   // Log.d("MakeManager", "本地注塑库已更新")
                }

                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭数据库连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //非注塑本地库更新
    private fun upZhusuOtherAllData(){
        val db = dbHelper.writableDatabase
        val zhusuList = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM zhusuothertable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameZhusu = rSet.getString("name")
                        zhusuList.add(nameZhusu)
                    }
                } catch (e: SQLException) {
                    //Log.e("MakeManager", "注塑名称插入失败")
                }
                for (name in zhusuList){
                    val values = contentValuesOf("produName" to name)
                    db.insert("nativeZhusuOther", null, values)
                    //Log.d("MakeManager","注塑名称插入成功")
                    //Log.d("MakeManager", "数据库内名称是：$name")
                }
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private fun upZhusuOtherData(){
        val db = dbHelper.writableDatabase
        val zhusuList1 = ArrayList<String>()
        val zhusuList2 = ArrayList<String>()
        val cursor = db.query("nativeZhusuOther",null,null,null,null,null,null)
        val nativeList = ArrayList<String>()
        val nativeList1 = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM zhusuothertable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameZhusu = rSet.getString("name")
                        zhusuList1.add(nameZhusu)
                    }
                } catch (e: SQLException) {
                  //  Log.e("MakeManager", "其他注塑名称搜索失败")
                }
                //Log.d("MakeManager", "远程其他注塑库准备完成")
                if (cursor.moveToFirst()){
                    do {
                        val name1 = cursor.getString(cursor.getColumnIndex("produName"))
                        nativeList.add(name1)
                    }while (cursor.moveToNext())
                }
                cursor.close()
                //Log.d("MakeManager", "本地其他注塑库准备完成")
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                if (zhusuList1.count() == nativeList.count()){
                    //判断需要更新本地还是远程库
                    nativeList.removeAll(zhusuList1)
                    zhusuList2.removeAll(nativeList1)
                    val count1 = nativeList.count()
                    val count2 = zhusuList2.count()
                    if (count1 > 0){
                        for (m in nativeList){
                            db.delete("nativeZhusuOther", "produName == ?", arrayOf(m))
                            //Log.d("MakeManager", "本地其他注塑库删除已更新")
                        }
                    }else if (count2 > 0){
                        for (n in zhusuList2){
                            val values = contentValuesOf("produName" to n)
                            db.insert("nativeZhusuOther", null, values)
                           // Log.d("MakeManager", "本地其他注塑库增加已更新")
                        }
                    }else{
                        //Log.d("MakeManager", "注塑数据库无更新")
                    }

                }else if (zhusuList1.count() > nativeList.count()){
                    zhusuList1.removeAll(nativeList)
                    for (i in zhusuList1){
                        val values = contentValuesOf("produName" to i)
                        db.insert("nativeZhusuOther", null, values)
                        //Log.d("MakeManager", "可更新的数据是：+$i+已更新至本地注塑库")
                    }
                }else{
                    nativeList.removeAll(zhusuList1)
                    for (i in nativeList)
                        db.delete("nativeZhusuOther", "produName == ?", arrayOf(i))
                    //Log.d("MakeManager", "本地注塑库已更新")
                }

                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭数据库连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
//吹塑本地库更新
private fun upChuisuAllData(){
    val db = dbHelper.writableDatabase
    val ChuisuList = ArrayList<String>()
    try {
        Thread ({
            val conn = DBUtil().conection()
            val sql = "SELECT * FROM chuisuprodunametable "
            try {
                // 创建用来执行sql语句的对象
                val statement: Statement = conn!!.createStatement()
                // 执行sql查询语句并获取查询信息
                val rSet: ResultSet = statement.executeQuery(sql)

                while (rSet.next()) {
                    val nameChuisu = rSet.getString("name")
                    ChuisuList.add(nameChuisu)
                }
            } catch (e: SQLException) {
               // Log.e("MakeManager", "吹塑名称插入失败")
            }
            for (name in ChuisuList){
                val values = contentValuesOf("produName" to name)
                db.insert("nativeChuisu", null, values)
               // Log.d("MakeManager","吹塑名称插入成功")
                //Log.d("MakeManager", "吹塑数据库内名称是：$name")
            }
            //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
            //关闭数据库
            try {
                conn!!.close()
               // Log.d("MakeManager", "关闭连接成功。")
            } catch (e: SQLException) {
              //  Log.d("MakeManager", "关闭连接失败。")
            }

        }).start()
    }catch (e:Exception){
        e.printStackTrace()
    }

}
    private fun upChuisuData(){
        val db = dbHelper.writableDatabase
        val chuisuList1 = ArrayList<String>()
        val chuisuList2 = ArrayList<String>()
        val cursor = db.query("nativeChuisu",null,null,null,null,null,null)
        val nativeChuisuList = ArrayList<String>()
        val nativeChuisuList1 = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM chuisuprodunametable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameChuisu = rSet.getString("name")
                        chuisuList1.add(nameChuisu)
                    }
                } catch (e: SQLException) {
                //    Log.e("MakeManager", "吹塑名称搜索失败")
                }
               // Log.d("MakeManager", "远程吹塑库准备完成")
                if (cursor.moveToFirst()){
                    do {
                        val name1 = cursor.getString(cursor.getColumnIndex("produName"))
                        nativeChuisuList.add(name1)
                    }while (cursor.moveToNext())
                }
                cursor.close()
              //  Log.d("MakeManager", "本地吹塑库准备完成")
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                if (chuisuList1.count() == nativeChuisuList.count()){
                    //判断需要更新本地还是远程库
                     nativeChuisuList.removeAll(chuisuList1)
                    chuisuList2.removeAll(nativeChuisuList1)
                    val count1 = nativeChuisuList.count()
                    val count2 = chuisuList2.count()
                    if (count1 > 0){
                        for (m in nativeChuisuList){
                            db.delete("nativeChuisu", "produName == ?", arrayOf(m))
                //            Log.d("MakeManager", "本地吹塑库删除已更新")
                        }
                    }else if (count2 > 0){
                        for (n in chuisuList2){
                            val values = contentValuesOf("produName" to n)
                            db.insert("nativeChuisu", null, values)
                   //         Log.d("MakeManager", "本地吹塑库增加已更新")
                        }
                    }else{
                  //      Log.d("MakeManager", "吹塑数据库无更新")
                    }

                }else if (chuisuList1.count() > nativeChuisuList.count()){
                    chuisuList1.removeAll(nativeChuisuList)
                    for (i in chuisuList1){
                        val values = contentValuesOf("produName" to i)
                        db.insert("nativeChuisu", null, values)
                        //Log.d("MakeManager", "可更新的数据是：+$i+已更新至本地吹塑库")
                    }
                }else{
                    nativeChuisuList.removeAll(chuisuList1)
                    for (i in nativeChuisuList)
                        db.delete("nativeChuisu", "produName == ?", arrayOf(i))
                  //  Log.d("MakeManager", "本地吹塑库已更新")
                }

                try {
                    conn!!.close()
                 //   Log.d("MakeManager", "关闭数据库连接成功。")
                } catch (e: SQLException) {
                 //   Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    //挤出本地库更新
    private fun upJichuAllData(){
        val db = dbHelper.writableDatabase
        val jichuList = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM jichuprodunametable"
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameJichu = rSet.getString("name")
                        jichuList.add(nameJichu)
                    }
                } catch (e: SQLException) {
                 //   Log.e("MakeManager", "挤出名称获取失败")
                }
                for (name in jichuList){
                    val values = contentValuesOf("produName" to name)
                    db.insert("nativeJichu", null, values)
                 //   Log.d("MakeManager","挤出名称插入成功")
                    //Log.d("MakeManager", "挤出数据库内名称是：$name")
                }
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private fun upJichuData(){
        val db = dbHelper.writableDatabase
        val jichuList1 = ArrayList<String>()
        val jichuList2 = ArrayList<String>()
        val cursor = db.query("nativeJichu",null,null,null,null,null,null)
        val nativeJichuList = ArrayList<String>()
        val nativeJichuList1 = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM jichuprodunametable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameJichu = rSet.getString("name")
                        jichuList1.add(nameJichu)
                    }
                } catch (e: SQLException) {
                  //  Log.e("MakeManager", "挤出名称搜索失败")
                }
               // Log.d("MakeManager", "远程挤出库准备完成")
                if (cursor.moveToFirst()){
                    do {
                        val name1 = cursor.getString(cursor.getColumnIndex("produName"))
                        nativeJichuList.add(name1)
                    }while (cursor.moveToNext())
                }
                cursor.close()
              //  Log.d("MakeManager", "本地挤出库准备完成")
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                if (jichuList1.count() == nativeJichuList.count()){
                    //判断需要更新本地还是远程库
                    nativeJichuList.removeAll(jichuList1)
                    jichuList2.removeAll(nativeJichuList1)
                    val count1 = nativeJichuList.count()
                    val count2 = jichuList2.count()
                    if (count1 > 0){
                        for (m in nativeJichuList){
                            db.delete("nativeJichu", "produName == ?", arrayOf(m))
                      //      Log.d("MakeManager", "本地挤出库删除已更新")
                        }
                    }else if (count2 > 0){
                        for (n in jichuList2){
                            val values = contentValuesOf("produName" to n)
                            db.insert("nativeJichu", null, values)
                       //     Log.d("MakeManager", "本地挤出库增加已更新")
                        }
                    }else{
                    //    Log.d("MakeManager", "挤出数据库无更新")
                    }

                }else if (jichuList1.count() > nativeJichuList.count()){
                    jichuList1.removeAll(nativeJichuList)
                    for (i in jichuList1){
                        val values = contentValuesOf("produName" to i)
                        db.insert("nativeJichu", null, values)
                       // Log.d("MakeManager", "可更新的数据是：+$i+已更新至本地挤出库")
                    }
                }else{
                    nativeJichuList.removeAll(jichuList1)
                    for (i in nativeJichuList)
                        db.delete("nativeJichu", "produName == ?", arrayOf(i))
                //    Log.d("MakeManager", "本地挤出库已更新")
                }

                try {
                    conn!!.close()
               //     Log.d("MakeManager", "关闭数据库连接成功。")
                } catch (e: SQLException) {
               //     Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    //其他本地库更新
    private fun upOtherAllData(){
        val db = dbHelper.writableDatabase
        val otherList = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM otherprodunametable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameOther = rSet.getString("name")
                        otherList.add(nameOther)
                    }
                } catch (e: SQLException) {
               //     Log.e("MakeManager", "其他名称插入失败")
                }
                for (name in otherList){
                    val values = contentValuesOf("produName" to name)
                    db.insert("nativeOther", null, values)
                //    Log.d("MakeManager","其他名称插入成功")
                    //Log.d("MakeManager", "其他数据库内名称是：$name")
                }
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                try {
                    conn!!.close()
             //       Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
            //        Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private fun upOtherData(){
        val db = dbHelper.writableDatabase
        val otherList1 = ArrayList<String>()
        val otherList2 = ArrayList<String>()
        val cursor = db.query("nativeOther",null,null,null,null,null,null)
        val nativeOtherList = ArrayList<String>()
        val nativeOtherList1 = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM otherprodunametable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameJichu = rSet.getString("name")
                        otherList1.add(nameJichu)
                    }
                } catch (e: SQLException) {
               //     Log.e("MakeManager", "其他名称搜索失败")
                }
               // Log.d("MakeManager", "远程其他库准备完成")
                if (cursor.moveToFirst()){
                    do {
                        val name1 = cursor.getString(cursor.getColumnIndex("produName"))
                        nativeOtherList.add(name1)
                    }while (cursor.moveToNext())
                }
                cursor.close()
               // Log.d("MakeManager", "本地其他库准备完成")
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                if (otherList1.count() == nativeOtherList.count()){
                    //判断需要更新本地还是远程库
                    nativeOtherList.removeAll(otherList1)
                    otherList2.removeAll(nativeOtherList1)
                    val count1 = nativeOtherList.count()
                    val count2 = otherList2.count()
                    if (count1 > 0){
                        for (m in nativeOtherList){
                            db.delete("nativeOther", "produName == ?", arrayOf(m))
                //            Log.d("MakeManager", "本地其他库已删除更新")
                        }
                    }else if (count2 > 0){
                        for (n in otherList2){
                            val values = contentValuesOf("produName" to n)
                            db.insert("nativeOther", null, values)
                  //          Log.d("MakeManager", "本地其他库已增加更新")
                        }
                    }else{
                //        Log.d("MakeManager", "其他数据库无更新")
                    }

                }else if (otherList1.count() > nativeOtherList.count()){
                    otherList1.removeAll(nativeOtherList)
                    for (i in otherList1){
                        val values = contentValuesOf("produName" to i)
                        db.insert("nativeOther", null, values)
                        //Log.d("MakeManager", "可更新的数据是：+$i+已更新至本地其他库")
                    }
                }else{
                    nativeOtherList.removeAll(otherList1)
                    for (i in nativeOtherList)
                        db.delete("nativeOther", "produName == ?", arrayOf(i))
                //    Log.d("MakeManager", "本地其他库已更新")
                }

                try {
                    conn!!.close()
                //    Log.d("MakeManager", "关闭数据库连接成功。")
                } catch (e: SQLException) {
                //    Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //颜色本地库更新
    private fun upColorAllData(){
        val db = dbHelper.writableDatabase
        val colorList = ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM colornametable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameOther = rSet.getString("name")
                        colorList.add(nameOther)
                    }
                } catch (e: SQLException) {
                //    Log.e("MakeManager", "颜色名称插入失败")
                }
                for (name in colorList){
                    val values = contentValuesOf("produColor" to name)
                    db.insert("nativeProduColor", null, values)
                 //   Log.d("MakeManager","颜色名称插入成功")
                    //Log.d("MakeManager", "颜色库内名称是：$name")
                }
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                try {
                    conn!!.close()
                 //   Log.d("MakeManager", "关闭连接成功。")
                } catch (e: SQLException) {
                //    Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private fun upColorData(){
        val db = dbHelper.writableDatabase
        val colorList1 = ArrayList<String>()
        val colorList2 = ArrayList<String>()
        val cursor = db.query("nativeProduColor",null,null,null,null,null,null)
        val nativeColorList = ArrayList<String>()
        val nativeColorList1 =ArrayList<String>()
        try {
            Thread ({
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM colornametable "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    while (rSet.next()) {
                        val nameColor = rSet.getString("name")
                        colorList1.add(nameColor)
                        colorList2.add(nameColor)
                    }
                } catch (e: SQLException) {
                  //  Log.e("MakeManager", "颜色名称搜索失败")
                }
              //  Log.d("MakeManager", "远程颜色库准备完成")
                if (cursor.moveToFirst()){
                    do {
                        val name1 = cursor.getString(cursor.getColumnIndex("produColor"))
                        nativeColorList.add(name1)
                        nativeColorList1.add(name1)
                    }while (cursor.moveToNext())
                }
                cursor.close()
              //  Log.d("MakeManager", "本地颜色库准备完成")
                //Log.d("MakeManager", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                //关闭数据库
                if (colorList1.count() == nativeColorList.count()){
                     nativeColorList.removeAll(colorList1)
                    colorList2.removeAll(nativeColorList1)
                    val count1 = nativeColorList.count()
                    val count2 = colorList2.count()
                    if (count1 > 0){
                        for (m in nativeColorList){
                            db.delete("nativeProduColor", "produColor == ?", arrayOf(m))
                        }
                //        Log.d("MakeManager", "颜色数据库更新完成")
                    }else if (count2 > 0){
                        for (n in colorList2){
                            val values = contentValuesOf("produColor" to n)
                            db.insert("nativeProduColor", null, values)
                        }
                    }else{
                 //       Log.d("MakeManager", "颜色数据库无更新")
                    }

                }else if (colorList1.count() > nativeColorList.count()){
                    colorList1.removeAll(nativeColorList)
                    for (i in colorList1){
                        val values = contentValuesOf("produColor" to i)
                        db.insert("nativeProduColor", null, values)
                        //Log.d("MakeManager", "可更新的数据是：+$i+已更新至本地颜色库")
                    }
                }else{
                    nativeColorList.removeAll(colorList1)
                    for (i in nativeColorList)
                        db.delete("nativeProduColor", "produColor == ?", arrayOf(i))
                //    Log.d("MakeManager", "本地颜色库已更新")
                }

                try {
                    conn!!.close()
             //       Log.d("MakeManager", "关闭数据库连接成功。")
                } catch (e: SQLException) {
             //       Log.d("MakeManager", "关闭连接失败。")
                }

            }).start()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}