package com.example.mynote

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.media.projection.MediaProjection
import android.widget.Toast

class DbManager {
    val Dbname="MyNotes"
    val DbTable="Notes"
    val colId="ID"
    val colTitle="Title"
    val colDes="Description"
    val dbVersion=1
    /// CREATE TABLE IF NOT EXISTS Notes (ID INTEGER PRIMARY KEY, Title TEXT,Description TEXT);
    val sqlCreateTable ="CREATE TABLE IF NOT EXISTS "+DbTable+" ("+colId+" INTEGER PRIMARY KEY,"+
            colTitle + " TEXT, "+colDes+" TEXT);"

    var  sqlDb:SQLiteDatabase?=null


    constructor(context: Context){
        var db=DatabaseHelperNote(context)
        sqlDb=db.writableDatabase
    }



    inner class DatabaseHelperNote:SQLiteOpenHelper{
       var  context:Context?=null
        constructor(context:Context):super(context,Dbname,null,dbVersion){
            this.context=context

        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"database is created",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS "+DbTable)
        }

    }

    fun Insert(values:ContentValues):Long{
        val ID=sqlDb!!.insert(DbTable,"",values)
        return ID

    }
    fun Query(projection: Array<String>,selection:String,selectionArgs:Array<String>,Sororder:String):Cursor{
        val qb=SQLiteQueryBuilder()
        qb.tables=DbTable
        val cursor=qb.query(sqlDb,projection,selection,selectionArgs,null,null,Sororder)
        return cursor
    }

    fun Delete(selection:String,selectionArgs:Array<String>):Int{
        val count=sqlDb!!.delete(DbTable,selection,selectionArgs)
        return count
    }
    fun Update(values: ContentValues,selection: String,selectionArgs: Array<String>):Int{
        val count=sqlDb!!.update(DbTable,values,selection,selectionArgs)
        return count
    }

}
