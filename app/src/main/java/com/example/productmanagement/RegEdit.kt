package com.example.productmanagement

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.reg_edit_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import org.jetbrains.anko.selector
import java.lang.Runnable
import java.sql.*
import kotlin.concurrent.thread


@Suppress("DEPRECATION")
class RegEdit : AppCompatActivity() {
    private val nameList = ArrayList<String>()

    val updateList1 = 1
    private val handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg1: Message) {
            when (msg1.what){
                updateList1 -> dialog()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_edit_layout)
        setSupportActionBar(toolbar)

        initdepartment()

        regBtm1.setOnClickListener {
            val keywordA = keyWord1.text.toString()
            val keywordB = keyWord2.text.toString()
            val nameEdit = nameReg.text.toString()
            if (nameEdit.isNotEmpty()){
                getUser()
                if (keywordA.isNotEmpty()){
                    if (keywordA == keywordB){
                         saveUser()
                        "注册成功！".showToast()

                    }else{
                        AlertDialog.Builder(this).apply {
                            setTitle("请注意！")
                            setMessage("两次输入的密码不相等！")
                            setCancelable((false))
                            setPositiveButton(("确定")){ _, _ -> }
                            show()
                        }
                    }
                }else{
                    AlertDialog.Builder(this).apply {
                        setTitle("请注意！")
                        setMessage("密码不能为空！")
                        setCancelable((false))
                        setPositiveButton(("确定")){ _, _ -> }
                        show()
                    }
                }
            }else{
                AlertDialog.Builder(this).apply {
                    setTitle("请注意！")
                    setMessage("姓名不能为空！")
                    setCancelable((false))
                    setPositiveButton(("确定")){ _, _ -> }
                    show()
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

    //警告对话框
    private fun dialog(){
        AlertDialog.Builder(this).apply {
            setTitle("请注意！")
            setMessage("名称重复")
            setCancelable((false))
            setPositiveButton(("确定")){ _, _ -> }
            show()
        }
    }

    //获取名单
    private fun getUser(){
        try {
            thread {
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM users"
                try {
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)
                    while (rSet.next()){
                        val searchName = rSet.getString("name")
                        nameList.add(searchName)
                    }
                    searchName()
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //姓名不为空时查询是否重名
    private fun searchName(){

        val nameEdit = nameReg.text.toString()

        if (nameList.contains(nameEdit)){
            val msg1 = Message()
            msg1.what = updateList1
            handler.sendMessage(msg1)
        }else{
            println(nameEdit)
        }
    }

    private fun initdepartment(){
        val departmentArray = listOf("其他", "生产", "品管", "仓库", "财务")
        departmentSelect.text = departmentArray[0]
        departmentSelect.setOnClickListener {
            selector("工序名称", departmentArray){i ->
                departmentSelect.text = departmentArray[i]
            }
        }
    }
    private fun saveUser(){
        var conn: Connection //一个成员变量

//加载数据库驱动
//加载数据库驱动
        try {
            Class.forName("com.mysql.jdbc.Driver")
            Log.d("MainActivity", "加载JDBC驱动成功！")
        } catch (e: ClassNotFoundException) {
            Log.d("MainActivity", "加载JDBC驱动失败！")
            return
        }
        //连接数据库（开辟一个新线程）
        //连接数据库（开辟一个新线程）
        val thread = Thread(Runnable {
            // 反复尝试连接，直到连接成功后退出循环
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(100) // 每隔0.1秒尝试连接
                } catch (e: InterruptedException) {
                    Log.e("MainActivity", e.toString())
                }

                // 2.设置好IP/端口/数据库名/用户名/密码等必要的连接信息
                val ip = "rm-bp164eysku8sarztm1o.mysql.rds.aliyuncs.com" //本机IP
                val port = 3306 //mysql默认端口
                val dbName = "produstore" //自己的数据库名
                val url = ("jdbc:mysql://" + ip + ":" + port
                        + "/" + dbName) // 构建连接mysql的字符串
                val user = "qdcksj_123" //自己的用户名
                val password = "CKbz87426619" //自己的密码

                // 3.连接JDBC
                try {
                    conn = DriverManager.getConnection(url, user, password)
                    Log.d("MainActivity", "连接数据库成功!")
                    //添加数据到users表
                    keyWord1.visibility = View.VISIBLE
                    keyWord2.visibility = View.VISIBLE
                    val name = nameReg.text.toString()
                    val keyword1 = keyWord1.text.toString()
                    val departmentReg = departmentSelect.text.toString()
                    val sql = "insert into  users(name, keyword, type) values('$name', '$keyword1', '$departmentReg')"
                    try {
                            // 创建用来执行sql语句的对象
                            val statement: Statement = conn.createStatement()
                            // 执行sql查询语句并获取查询信息
                            val num = statement.executeUpdate(sql)
                            if (num>0){
                                Log.d("MainActivity", "成功写入数据")
                            }
                    } catch (e: SQLException) {
                        Log.e("MainActivity", "写入错误")
                    }


                    //关闭数据库
                    try {
                        conn.close()
                        Log.d("MainActivity", "关闭连接成功。")
                    } catch (e: SQLException) {
                        Log.d("MainActivity", "关闭连接失败。")
                    }
                    return@Runnable
                } catch (e: SQLException) {
                    Log.d("MainActivity", "连接数据库失败!")
                }
            }
        })
        thread.start()
    }
}