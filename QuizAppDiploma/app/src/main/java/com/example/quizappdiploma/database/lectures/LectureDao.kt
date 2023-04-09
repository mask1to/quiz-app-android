package com.example.quizappdiploma.database.lectures

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LectureDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLecture(lecture : LectureModel)
    @Query("SELECT * FROM lectures WHERE lectures.course_id = :courseId")
    fun getLecturesByCourseId(courseId: Int) : LiveData<List<LectureModel>>

    @Query("SELECT lecture_name FROM lectures WHERE course_id= :courseId")
    fun getLectureNameByCourseId(courseId : Int) : LiveData<List<LectureModel>>

    @Query("SELECT * FROM lectures WHERE lecture_name = :lectureName")
    fun getLectureDescByLectureName(lectureName : String) : LiveData<List<LectureModel>>
    @Update
    suspend fun updateLecture(lecture: LectureModel)
    @Delete
    suspend fun deleteLecture(lecture: LectureModel)

}