package com.example.quizappdiploma.database.courses

import androidx.lifecycle.LiveData

class CourseDataRepository(private val courseDao: CourseDao)
{
    suspend fun addCourse(course : CourseModel)
    {
        return courseDao.addCourse(course)
    }
    suspend fun updateCourse(course: CourseModel)
    {
        return courseDao.updateCourse(course)
    }
    suspend fun deleteCourse(course: CourseModel)
    {
        return courseDao.deleteCourse(course)
    }
    fun getCoursesByIdAsc() : LiveData<List<CourseModel>>
    {
        return courseDao.getCoursesOrderByIdAsc()
    }
    fun getCourseByName(courseName : String) : LiveData<List<CourseModel>>
    {
        return courseDao.getCourseByName(courseName)
    }

    fun getCourseNames() : LiveData<List<CourseModel>>
    {
        return courseDao.getCourseNames()
    }

    fun getCourseIdByName(courseName : String) : LiveData<List<CourseModel>>
    {
        return courseDao.getCourseIdByName(courseName)
    }
}