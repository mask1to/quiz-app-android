package com.example.quizappdiploma.database.lectures

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LectureDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLecture(lecture : LectureModel)
    @Query("SELECT * FROM lectures JOIN courses ON lectures.course_id = courses.id WHERE courses.course_name = :courseName")
    fun getLecturesByCourseName(courseName : String) : LiveData<List<LectureModel>>
    @Query("SELECT * FROM lectures WHERE lectures.course_id = :courseId")
    fun getLecturesByCourseId(courseId: Int) : LiveData<List<LectureModel>>
    @Update
    suspend fun updateLecture(lecture: LectureModel)
    @Delete
    suspend fun deleteLecture(lecture: LectureModel)

}