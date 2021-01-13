package com.example.productmanagement

import android.content.Intent
import android.os.Bundle
import androidx.core.content.contentValuesOf
import kotlinx.android.synthetic.main.activity_login_page.*
import java.sql.*

class LoginPage : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        supportActionBar?.hide()

        logoutBtm.setOnClickListener {
            ActivityCollector.finishAll()
        }
//        regBtm.setOnClickListener {
//            val intent = Intent(this,RegEdit::class.java)
//            startActivity(intent)
//        }

        loginBtm.setOnClickListener {
            loginto()
        }
    }
    private fun loginto(){
        val dbHelper = ProduDatabaseHelper(this, "nativeBases", 1)
        val db = dbHelper.writableDatabase
        try {
            Thread {
                val conn = DBUtil().conection()
                val sql = "SELECT * FROM users "
                try {
                    // 创建用来执行sql语句的对象
                    val statement: Statement = conn!!.createStatement()
                    // 执行sql查询语句并获取查询信息
                    val rSet: ResultSet = statement.executeQuery(sql)

                    val name = nameEdit.text.toString()
                    val keyword = keywordEdit.text.toString()

                    while (rSet.next()) {

                        // Log.d("MainActivity", rSet.getString("id").toString() + "\t" + rSet.getString("name") + "\t")
                        if (name == rSet.getString("name")){
                            // Log.d("LoginPage", "用户名正确")
                            if (keyword == rSet.getString("keyword")){
                                // Log.d("LoginPage", "密码正确")
                                if (rSet.getString("type") == "超级管理"){
                                    val intent = Intent(this,SuperManager::class.java)
                                    startActivity(intent)
                                }else{
                                    val intent = Intent(this,MakeManager::class.java)
                                    startActivity(intent)
                                    db.delete("nativeUser", null,null)
                                    val values = contentValuesOf("userName" to name)
                                    db.insert("nativeUser",null, values)
                                }
                            }
                        }
                    }

                    // 迭代打印出查询信息
                    // Log.d("MainActivity", "用户表")
                    //Log.d("MainActivity", "id\t\t\tname\tkeyword\t")

                } catch (e: SQLException) {
                    //Log.e("LoginPage", "查询错误")
                }


                //关闭数据库
                try {
                    conn!!.close()
                    //Log.d("LoginPage", "关闭连接成功。")
                } catch (e: SQLException) {
                    //Log.d("LoginPage", "关闭连接失败。")
                }

            }.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}