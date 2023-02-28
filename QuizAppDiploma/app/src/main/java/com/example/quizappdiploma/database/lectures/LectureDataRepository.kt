package com.example.quizappdiploma.database.lectures

import androidx.lifecycle.LiveData
import com.example.quizappdiploma.database.MyLocalCache
import com.example.quizappdiploma.database.courses.CourseDao
import com.example.quizappdiploma.database.courses.CourseDataRepository

class LectureDataRepository(private val lectureDao: LectureDao)
{
    suspend fun addLecture(lecture : LectureModel)
    {
        lectureDao.addLecture(lecture)
    }

    suspend fun updateLecture(lecture: LectureModel)
    {
        lectureDao.updateLecture(lecture)
    }

    suspend fun deleteLecture(lecture: LectureModel)
    {
        lectureDao.deleteLecture(lecture)
    }
    fun getLecturesByCourseId(courseId: Int) : LiveData<List<LectureModel>>
    {
        return lectureDao.getLecturesByCourseId(courseId)
    }
}