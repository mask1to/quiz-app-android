package com.example.quizappdiploma.database.courses

import androidx.lifecycle.LiveData

class CourseDataRepository(private val courseDao: CourseDao)
{
    val readAllData : LiveData<List<CourseModel>> = courseDao.readAllData()

    suspend fun addCourse(course : CourseModel)
    {
        courseDao.addCourse(course)
    }
}