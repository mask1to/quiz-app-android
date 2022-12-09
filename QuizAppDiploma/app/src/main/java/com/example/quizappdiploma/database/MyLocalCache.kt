package com.example.quizappdiploma.database

import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureModel

class MyLocalCache(val databaseDao: MyDatabaseDao)
{
    suspend fun insertCourses(courses : List<CourseModel>)
    {
        databaseDao.insertCourses(courses)
    }

    suspend fun insertLectures(lectures : List<LectureModel>)
    {
        databaseDao.insertLectures(lectures)
    }

    suspend fun deleteCourses()
    {
        databaseDao.deleteCourses()
    }

    suspend fun deleteLectures()
    {
        databaseDao.deleteLectures()
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

    suspend fun getCourses()
    {

    }

    suspend fun getLectures()
    {

    }
}