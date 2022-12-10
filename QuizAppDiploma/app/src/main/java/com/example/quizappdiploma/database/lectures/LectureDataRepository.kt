package com.example.quizappdiploma.database.lectures

import com.example.quizappdiploma.database.MyLocalCache
import com.example.quizappdiploma.database.courses.CourseDataRepository

class LectureDataRepository private constructor(cache: MyLocalCache)
{
    companion object{
        @Volatile
        private var INSTANCE : LectureDataRepository? = null

        fun getInstance(cache: MyLocalCache) : LectureDataRepository =
            INSTANCE ?: synchronized(this)
            {
                INSTANCE ?: LectureDataRepository(cache).also { INSTANCE = it }
            }
    }
}