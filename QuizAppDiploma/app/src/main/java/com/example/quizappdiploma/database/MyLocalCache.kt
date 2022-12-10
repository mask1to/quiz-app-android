package com.example.quizappdiploma.database

import androidx.lifecycle.LiveData
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.database.users.UserModel

class MyLocalCache(val databaseDao: MyDatabaseDao)
{
    fun insertCourses(courses : List<CourseModel>)
    {
        databaseDao.insertCourses(courses)
    }

    fun insertLectures(lectures : List<LectureModel>)
    {
        databaseDao.insertLectures(lectures)
    }

    fun insertUsers(users : List<UserModel>)
    {
        databaseDao.insertUsers(users)
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

    suspend fun deleteQuizAnswers()
    {
        databaseDao.deleteQuizAnswers()
    }

    suspend fun deleteQuizQuestions()
    {
        databaseDao.deleteQuizQuestions()
    }

    suspend fun getCourses() : LiveData<List<CourseModel>?> = databaseDao.getCourses()


    suspend fun getLectures()
    {

    }
}