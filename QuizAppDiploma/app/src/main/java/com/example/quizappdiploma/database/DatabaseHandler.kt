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
import com.example.quizappdiploma.database.DatabaseConstants
import java.lang.Exception

class DatabaseHandler(context: Context):SQLiteOpenHelper(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION)
{


    override fun onCreate(db: SQLiteDatabase?)
    {
        val createUserTable = ("CREATE TABLE "
                                   + DatabaseConstants.TBL_USERS + "("
                                   + DatabaseConstants.KEY_ID_STUDENT + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                   + DatabaseConstants.KEY_USERNAME + "TEXT NOT NULL UNIQUE,"
                                   + DatabaseConstants.KEY_PASSWORD + "TEXT NOT NULL,"
                                   + DatabaseConstants.KEY_EMAIL + "TEXT NOT NULL,"
                                   + DatabaseConstants.KEY_STUDENT + "INTEGER,"
                                   + DatabaseConstants.KEY_LECTURER + "INTEGER,"
                                   + DatabaseConstants.KEY_ADMIN + "INTEGER"
                                   + ")"
                                 )

        val createCourseTable = ("CREATE TABLE "
                                    + DatabaseConstants.TBL_COURSE + "("
                                    + DatabaseConstants.KEY_ID_COURSE + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                    + DatabaseConstants.KEY_NAME_COURSE + "TEXT NOT NULL UNIQUE,"
                                    + DatabaseConstants.KEY_IMAGE_ID_COURSE + "INTEGER NOT NULL"
                                    + ")"
                                )

        val createLectureTable = ("CREATE TABLE "
                                    + DatabaseConstants.TBL_LECTURE + "("
                                    + DatabaseConstants.KEY_ID_LECTURE + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                    + DatabaseConstants.KEY_NAME_LECTURE + "TEXT NOT NULL UNIQUE,"
                                    + DatabaseConstants.KEY_DESC_LECTURE + "TEXT NOT NULL,"
                                    + DatabaseConstants.KEY_IMAGE_ID_LECTURE + "INTEGER"
                                    + ")"
                                 )

        val createQuizTable = ("CREATE TABLE "
                                    + DatabaseConstants.TBL_QUIZ + "("
                                    + DatabaseConstants.KEY_ID_QUIZ + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                    + DatabaseConstants.QUIZ_NAME + "TEXT NOT NULL UNIQUE,"
                                    + DatabaseConstants.QUIZ_ACTIVE + "INTEGER NOT NULL,"
                                    + DatabaseConstants.QUIZ_DIFFICULTY + "INTEGER NOT NULL"
                                    + ")")

        val createQuizQuestionsTable = ("CREATE TABLE "
                                    + DatabaseConstants.TBL_QUIZ_QUESTIONS + "("
                                    + DatabaseConstants.KEY_ID_QUESTION + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                    + DatabaseConstants.QUESTION_NAME + "TEXT NOT NULL UNIQUE,"
                                    + DatabaseConstants.QUESTION_POINTS + "INTEGER NOT NULL,"
                                    + DatabaseConstants.QUESTION_DIFFICULTY + "INTEGER NOT NULL,"
                                    + DatabaseConstants.QUESTION_MULTI_CHOICE + "INTEGER NOT NULL"
                                    + ")")

        val createQuizQuestionAnswersTable = ("CREATE TABLE "
                                    + DatabaseConstants.TBL_QUIZ_QUESTION_ANSWERS + "("
                                    + DatabaseConstants.KEY_ID_ANSWER + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                    + DatabaseConstants.ANSWER + "TEXT NOT NULL,"
                                    + DatabaseConstants.ANSWER_CORRECT + "INTEGER NOT NULL"
                                    + ")")

        db?.execSQL(createUserTable)
        db?.execSQL(createCourseTable)
        db?.execSQL(createLectureTable)
        db?.execSQL(createQuizTable)
        db?.execSQL(createQuizQuestionsTable)
        db?.execSQL(createQuizQuestionAnswersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db!!.execSQL("DROP TABLE IF EXISTS ${DatabaseConstants.TBL_USERS}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseConstants.TBL_COURSE}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseConstants.TBL_LECTURE}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseConstants.TBL_QUIZ}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseConstants.TBL_QUIZ_QUESTIONS}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseConstants.TBL_QUIZ_QUESTION_ANSWERS}")
        onCreate(db)
    }


    fun insertNewUser(student : Student) : Long
    {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        //contentValues.put(KEY_ID_STUDENT, student.id)
        contentValues.put(DatabaseConstants.KEY_USERNAME, student.username)
        contentValues.put(DatabaseConstants.KEY_PASSWORD, student.password)
        contentValues.put(DatabaseConstants.KEY_EMAIL, student.email)
        contentValues.put(DatabaseConstants.KEY_STUDENT, student.isStudent)
        contentValues.put(DatabaseConstants.KEY_LECTURER, student.isLecturer)
        contentValues.put(DatabaseConstants.KEY_ADMIN, student.isAdmin)

        val success = db.insert(DatabaseConstants.TBL_USERS, null, contentValues)
        db.close()

        return success
    }

    fun updateUser(student : Student) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseConstants.KEY_USERNAME, student.username)
        contentValues.put(DatabaseConstants.KEY_EMAIL, student.email)
        contentValues.put(DatabaseConstants.KEY_PASSWORD, student.password)
        contentValues.put(DatabaseConstants.KEY_STUDENT, student.isStudent)
        contentValues.put(DatabaseConstants.KEY_LECTURER, student.isLecturer)
        contentValues.put(DatabaseConstants.KEY_ADMIN, student.isAdmin)

        val success = db.update(DatabaseConstants.TBL_USERS, contentValues, DatabaseConstants.KEY_USERNAME + "=" + student.username, null)

        db.close()
        return success
    }

    fun updateCourse(course : Course) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseConstants.KEY_NAME_COURSE, course.name)
        contentValues.put(DatabaseConstants.KEY_IMAGE_ID_COURSE, course.image)

        val success = db.update(DatabaseConstants.TBL_COURSE, contentValues, DatabaseConstants.KEY_NAME_COURSE + "=" + course.name, null)

        db.close()
        return success
    }

    fun updateLecture(lecture : Lecture) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseConstants.KEY_NAME_LECTURE, lecture.name)
        contentValues.put(DatabaseConstants.KEY_DESC_LECTURE, lecture.description)
        contentValues.put(DatabaseConstants.KEY_IMAGE_ID_LECTURE, lecture.image)

        val success = db.update(DatabaseConstants.TBL_LECTURE, contentValues, DatabaseConstants.KEY_NAME_LECTURE + "=" + lecture.name, null)

        db.close()
        return success
    }

    fun deleteUser(student : Student) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseConstants.KEY_USERNAME, student.username)

        val success = db.delete(DatabaseConstants.TBL_USERS, DatabaseConstants.KEY_USERNAME + "=" + student.username, null)

        db.close()
        return success
    }

    fun deleteCourse(course : Course) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseConstants.KEY_NAME_COURSE, course.name)

        val success = db.delete(DatabaseConstants.TBL_COURSE, DatabaseConstants.KEY_NAME_COURSE + "=" + course.name, null)

        db.close()
        return success
    }

    fun deleteLecture(lecture : Lecture) : Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseConstants.KEY_NAME_LECTURE, lecture.name)

        val success = db.delete(DatabaseConstants.TBL_LECTURE, DatabaseConstants.KEY_NAME_LECTURE + "=" + lecture.name, null)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllUsers():ArrayList<Student>
    {
        val studentList : ArrayList<Student> = ArrayList()
        val selectQuery = "SELECT * FROM ${DatabaseConstants.TBL_USERS}"
        val db = this.readableDatabase

        val cursor : Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e : Exception){
          e.printStackTrace()
          db.execSQL(selectQuery)
          return ArrayList()
        }

        var username : String
        var email : String
        var password : String
        var isStudent : Int
        var isLecturer : Int
        var isAdmin : Int

        if(cursor.moveToFirst())
        {
            do{
                username = cursor.getString(cursor.getColumnIndex("username"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                password = cursor.getString(cursor.getColumnIndex("password"))
                isStudent = cursor.getInt(cursor.getColumnIndex("isStudent"))
                isLecturer = cursor.getInt(cursor.getColumnIndex("isLecturer"))
                isAdmin = cursor.getInt(cursor.getColumnIndex("isAdmin"))

                val student = Student(username = username, email = email, password = password,
                isStudent = isStudent, isLecturer = isLecturer, isAdmin = isAdmin)

                studentList.add(student)

            } while (cursor.moveToNext())
        }

        return studentList
    }


    fun getCourses() : Cursor?
    {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM ${DatabaseConstants.TBL_COURSE}", null)
    }



}
