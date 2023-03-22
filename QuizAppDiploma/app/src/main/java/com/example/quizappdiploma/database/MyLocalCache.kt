package com.example.quizappdiploma.database

import androidx.lifecycle.LiveData
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.database.users.UserModel

class MyLocalCache(private val databaseDao: MyDatabaseDao)
{
    fun insertCourses(courses : List<CourseModel>)
    {
        databaseDao.insertCourses(courses)
    }

    fun insertLectures(lectures : List<LectureModel>)
    {
        databaseDao.insertLectures(lectures)
    }

    suspend fun insertUser(userModel: UserModel)
    {
        databaseDao.insertUser(userModel)
    }

    suspend fun readAllData()
    {
        databaseDao.readAllData()
    }

    fun deleteCourses()
    {
        databaseDao.deleteCourses()
    }

    fun deleteLectures()
    {
        databaseDao.deleteLectures()
    }

    fun deleteUsers()
    {
        databaseDao.deleteUsers()
    }

    suspend fun deleteQuizzes()
    {
        databaseDao.deleteQuizzes()
    }
    suspend fun deleteQuizQuestions()
    {
        databaseDao.deleteQuizQuestions()
    }

}