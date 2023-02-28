package com.example.quizappdiploma.database.courses

import androidx.lifecycle.LiveData

class CourseDataRepository(private val courseDao: CourseDao)
{
    suspend fun addCourse(course : CourseModel)
    {
        courseDao.addCourse(course)
    }
    suspend fun updateCourse(course: CourseModel)
    {
        courseDao.updateCourse(course)
    }
    suspend fun deleteCourse(course: CourseModel)
    {
        courseDao.deleteCourse(course)
    }

    fun getCoursesByIdAsc() : LiveData<List<CourseModel>>
    {
        return courseDao.getCoursesOrderByIdAsc()
    }

}