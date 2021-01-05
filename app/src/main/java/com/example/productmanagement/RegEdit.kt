package com.example.productmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.android.synthetic.main.reg_edit_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.*
import kotlinx.android.synthetic.main.super_manager_layout.toolbar
import org.jetbrains.anko.selector
import java.sql.*

class RegEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_edit_layout)
        setSupportActionBar(toolbar)

        initdepartment()

        regBtm1.setOnClickListener {
            saveUser()
        }

    }
    fun initdepartment(){
        val departmentArray = listOf("选择部门", "生产", "仓库", "品管", "财务", "总经理")
        departmentSelect.text = departmentArray[0]
        departmentSelect.setOnClickListener {
            selector("工序名称", departmentArray){i ->
                departmentSelect.text = departmentArray[i]
            }
        }
    }
    fun saveUser(){
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
                    val name = nameReg.text.toString()
                    val keyword = keyWordReg.text.toString()
                    val departmentReg = departmentSelect.text.toString()
                    val sql = "insert into  users(name, keyword, type) values('$name', '$keyword', '$departmentReg')"
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