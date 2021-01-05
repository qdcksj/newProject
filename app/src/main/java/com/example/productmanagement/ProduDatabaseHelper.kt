package com.example.productmanagement

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class ProduDatabaseHelper(val context:Context, name:String, version: Int):SQLiteOpenHelper(context, name, null, version) {

    //产品名称库6、7、8、9
    private val createnativeZhusu = "create table nativeZhusu(" +
            "id integer primary key autoincrement," +
            "produName text)"  //注塑瓶坯产品名称
    private val createnativeZhusuOther = "create table nativeZhusuOther(" +
            "id integer primary key autoincrement," +
            "produName text)"  //其他注塑产品名称
    private val createnativeChuisu = "create table nativeChuisu(" +
            "id integer primary key autoincrement," +
            "produName text)"  //产品名称
    private val createnativeJichu = "create table nativeJichu(" +
            "id integer primary key autoincrement," +
            "produName text)"  //产品名称
    private val createnativeOther = "create table nativeOther(" +
            "id integer primary key autoincrement," +
            "produName text)"  //产品名称
    //产品颜色库10
    private val createnativeProduColor = "create table nativeProduColor(" +
            "id integer primary key autoincrement," +
            "produColor text)"  //产品名称
    private val createnativeType = "create table nativeType(" +
            "id integer primary key autoincrement," +
            "typeName text)"  //产品名称
    private val createnativeUser = "create table nativeUser(" +
            "id integer primary key autoincrement," +
            "userName text)"  //本地用户名


    //临时用表
    private val createnativeOutTable = "create table nativeOutTable(" +
            "id integer primary key autoincrement," +
            "lineName text," +   //工序名称
            "produName text," +     //产品名称
            "pingpiName text," +     //瓶坯名称
            "produColor text," +       //产品颜色
            "outAmount integer," +   //数量
            "produMenu text)"       //备注说明

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(createnativeZhusu)
        db.execSQL(createnativeChuisu)
        db.execSQL(createnativeJichu)
        db.execSQL(createnativeOther)
        db.execSQL(createnativeProduColor)
        db.execSQL(createnativeOutTable)
        db.execSQL(createnativeType)
        db.execSQL(createnativeZhusuOther)
        db.execSQL(createnativeUser)


        Log.d("MainActivity","数据表创建成功")


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersoin: Int) {

    }

}