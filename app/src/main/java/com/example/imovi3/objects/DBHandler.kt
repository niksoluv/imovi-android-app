package com.example.imovi2.objects

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "MyDB"
val TABLE_NAME = "Films"
val COL_ID = "id"
val COL_FILMID = "film_id"

class DBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FILMID + " VARCHAR(16))"

        db?.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(film: Films){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_FILMID, film.filmId)
        var result = db.insert(TABLE_NAME, null, cv)
        if(result==-1.toLong())
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show()
    }

    fun readData() : MutableList<Films>{
        var list: MutableList<Films> = ArrayList()
        val db = this.readableDatabase
        var query = "select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)

        if(result.moveToFirst()){
            do{
                var film = Films()
                film.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                film.filmId = result.getString(result.getColumnIndex(COL_FILMID)).toString()
                list.add(film)
            }while(result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun deleteData(film: Films){
        val db = this.writableDatabase

        db.delete(TABLE_NAME, COL_FILMID + "=?", arrayOf(film.filmId))

        db.close()
        Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show()
    }

}