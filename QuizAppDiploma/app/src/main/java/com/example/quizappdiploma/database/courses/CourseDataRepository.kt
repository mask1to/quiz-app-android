package com.example.quizappdiploma.database.courses

import com.example.quizappdiploma.database.MyLocalCache

class CourseDataRepository private constructor(private val cache : MyLocalCache)
{
    companion object{
        @Volatile
        private var INSTANCE : CourseDataRepository? = null

        fun getInstance(cache: MyLocalCache) : CourseDataRepository =
            INSTANCE ?: synchronized(this)
            {
                INSTANCE ?: CourseDataRepository(cache).also { INSTANCE = it }
            }
    }

    fun insertCourse(courseModel: CourseModel)
    {

    }

    fun updateCourse(courseModel: CourseModel)
    {

    }

    fun deleteCourse(courseModel: CourseModel)
    {

    }

    fun deleteAllCourses()
    {

    }
}