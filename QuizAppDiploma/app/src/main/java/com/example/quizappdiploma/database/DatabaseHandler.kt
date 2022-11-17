package com.example.quizappdiploma.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quizappdiploma.entities.Course
import com.example.quizappdiploma.entities.Lecture
import com.example.quizappdiploma.entities.Student
import java.lang.Exception

class DatabaseHandler(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "learningnow3"
        private const val TBL_USERS = "tbl_users"
        private const val TBL_COURSE = "tbl_course"
        private const val TBL_LECTURE = "tbl_lecture"

        /**
         * Table users
         */
        private const val KEY_ID_STUDENT = "id "
        private const val KEY_USERNAME = "username "
        private const val KEY_EMAIL = "email "
        private const val KEY_PASSWORD = "password "
        private const val KEY_STUDENT = "isStudent "
        private const val KEY_LECTURER = "isLecturer "
        private const val KEY_ADMIN = "isAdmin "

        /**
         * Table course
         */
        private const val KEY_ID_COURSE = "id "
        private const val KEY_NAME_COURSE = "name "
        private const val KEY_IMAGE_ID_COURSE = "image_id "

        /**
         * Table lecture
         */
        private const val KEY_ID_LECTURE = "id "
        private const val KEY_NAME_LECTURE = "name "
        private const val KEY_DESC_LECTURE = "content_description "
        private const val KEY_IMAGE_ID_LECTURE = "image_id "


    }

    override fun onCreate(db: SQLiteDatabase?)
    {
        val createUserTable = ("CREATE TABLE " + TBL_USERS + "("
                                   + KEY_ID_STUDENT + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                   + KEY_USERNAME + "TEXT NOT NULL,"
                                   + KEY_PASSWORD + "TEXT NOT NULL,"
                                   + KEY_EMAIL + "TEXT NOT NULL,"
                                   + KEY_STUDENT + "INTEGER,"
                                   + KEY_LECTURER + "INTEGER,"
                                   + KEY_ADMIN + "INTEGER"
                                    + ")"
                                 )

        val createCourseTable = ("CREATE TABLE " + TBL_COURSE + "("
                                    + KEY_ID_COURSE + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                    + KEY_NAME_COURSE + "TEXT NOT NULL,"
                                    + KEY_IMAGE_ID_COURSE + "INTEGER NOT NULL"
                                    + ")"
                                )

        val createLectureTable = ("CREATE TABLE " + TBL_LECTURE + "("
                                    + KEY_ID_LECTURE + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                    + KEY_NAME_LECTURE + "TEXT NOT NULL,"
                                    + KEY_DESC_LECTURE + "TEXT NOT NULL,"
                                    + KEY_IMAGE_ID_LECTURE + "INTEGER"
                                    + ")"
                                 )

        db?.execSQL(createUserTable)
        db?.execSQL(createCourseTable)
        db?.execSQL(createLectureTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TBL_COURSE")
        db.execSQL("DROP TABLE IF EXISTS $TBL_LECTURE")
        onCreate(db)
    }


    fun insertNewUser(student : Student) : Long
    {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        //contentValues.put(KEY_ID_STUDENT, student.id)
        contentValues.put(KEY_USERNAME, student.username)
        contentValues.put(KEY_PASSWORD, student.password)
        contentValues.put(KEY_EMAIL, student.email)
        contentValues.put(KEY_STUDENT, student.isStudent)
        contentValues.put(KEY_LECTURER, student.isLecturer)
        contentValues.put(KEY_ADMIN, student.isAdmin)

        val success = db.insert(TBL_USERS, null, contentValues)
        db.close()

        return success
    }

    /*fun updateUser(student : Student) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_USERNAME, student.username)
        contentValues.put(KEY_EMAIL, student.email)
        contentValues.put(KEY_PASSWORD, student.password)
        contentValues.put(KEY_STUDENT, student.isStudent)
        contentValues.put(KEY_LECTURER, student.isLecturer)
        contentValues.put(KEY_ADMIN, student.isAdmin)

        val success = db.update(TBL_USERS, contentValues, KEY_ID_STUDENT + "=" + student.id, null)

        db.close()
        return success
    }

    fun updateCourse(course : Course) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME_COURSE, course.name)
        contentValues.put(KEY_IMAGE_ID_COURSE, course.image)

        val success = db.update(TBL_COURSE, contentValues, KEY_ID_COURSE + "=" + course.id, null)

        db.close()
        return success
    }

    fun updateLecture(lecture : Lecture) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME_LECTURE, lecture.name)
        contentValues.put(KEY_DESC_LECTURE, lecture.description)
        contentValues.put(KEY_IMAGE_ID_LECTURE, lecture.image)

        val success = db.update(TBL_LECTURE, contentValues, KEY_ID_LECTURE + "=" + lecture.id, null)

        db.close()
        return success
    }

    fun deleteUser(student : Student) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID_STUDENT, student.id)

        val success = db.delete(TBL_USERS, KEY_ID_STUDENT + "=" + student.id, null)

        db.close()
        return success
    }

    fun deleteCourse(course : Course) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID_COURSE, course.id)

        val success = db.delete(TBL_COURSE, KEY_ID_COURSE + "=" + course.id, null)

        db.close()
        return success
    }

    fun deleteLecture(lecture : Lecture) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID_LECTURE, lecture.id)

        val success = db.delete(TBL_LECTURE, KEY_ID_LECTURE + "=" + lecture.id, null)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllUsers():ArrayList<Student>
    {
        val studentList : ArrayList<Student> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_USERS"
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
        var username : String
        var email : String
        var password : String
        var isStudent : Int
        var isLecturer : Int
        var isAdmin : Int

        if(cursor.moveToFirst())
        {
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                username = cursor.getString(cursor.getColumnIndex("username"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                password = cursor.getString(cursor.getColumnIndex("password"))
                isStudent = cursor.getInt(cursor.getColumnIndex("isStudent"))
                isLecturer = cursor.getInt(cursor.getColumnIndex("isLecturer"))
                isAdmin = cursor.getInt(cursor.getColumnIndex("isAdmin"))

                val student = Student(id = id, username = username, email = email, password = password,
                isStudent = isStudent, isLecturer = isLecturer, isAdmin = isAdmin)

                studentList.add(student)

            } while (cursor.moveToNext())
        }

        return studentList
    }
    */
    fun getCourses() : Cursor?
    {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $TBL_COURSE", null)
    }



}
