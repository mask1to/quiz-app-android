package com.example.quizappdiploma.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quizappdiploma.entities.Student
import java.lang.Exception

class DatabaseHandler(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "quizapp.db"
        private const val TBL_STUDENT = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?)
    {
        val createTableStudent = ("CREATE TABLE " + TBL_STUDENT + "("
                                   + ID + "INTEGER PRIMARY KEY,"
                                   + NAME + "TEXT,"
                                   + EMAIL + "TEXT" + ")"
                                 )
        db?.execSQL(createTableStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(db)
    }

    /*fun insertNewStudent(student : Student) : Long
    {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, student.id)
        contentValues.put(NAME, student.name)
        contentValues.put(EMAIL, student.email)

        val success = db.insert(TBL_STUDENT, null, contentValues)
        db.close()
        return success
    }*/

    //@SuppressLint("Range")
    /*fun getAllStudents():ArrayList<Student>
    {
        val studentList : ArrayList<Student> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_STUDENT"
        val db = this.readableDatabase

        val cursor : Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e : Exception){
          e.printStackTrace()
          db.execSQL(selectQuery)
          return ArrayList()
        }

        var id : Int
        var name : String
        var email : String

        if(cursor.moveToFirst())
        {
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))

                val student = Student(id = id, name = name, email = email)
                studentList.add(student)
            } while (cursor.moveToNext())
        }

        return studentList
    }*/

}
