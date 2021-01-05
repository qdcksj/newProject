package com.example.productmanagement

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DBUtil {
    var conn: Connection? = null
    val ip = "rm-bp164eysku8sarztm1o.mysql.rds.aliyuncs.com" //远程IP
    val port = 3306 //mysql默认端口
    val dbName = "produstore" //自己的数据库名
    val url = ("jdbc:mysql://" + ip + ":" + port
            + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8&useSSL=true") // 构建连接mysql的字符串
    val user = "qdcksj_123" //自己的用户名
    val password = "CKbz87426619" //自己的密码
    //加载数据库驱动
    fun conection():java.sql.Connection? {
        try {
            Class.forName("com.mysql.jdbc.Driver")
            //Log.d("MainActivity", "加载JDBC驱动成功！")
        } catch (e: ClassNotFoundException) {
            //Log.d("MainActivity", "加载JDBC驱动失败！")
        }
        //连接JDBC
        try {
            conn = DriverManager.getConnection(url, user, password)
            //Log.d("MainActivity", "连接数据库成功!")

        } catch (e: SQLException) {
           // Log.d("MainActivity", "连接数据库失败!")
        }
        return conn
    }
}