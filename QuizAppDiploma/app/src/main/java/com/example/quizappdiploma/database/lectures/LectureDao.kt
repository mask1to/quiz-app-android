package com.example.quizappdiploma.database.lectures

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LectureDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLecture(lecture : LectureModel)

    @Query("SELECT * FROM lectures WHERE course_id = 1 ORDER BY lecture_name ASC")
    fun readAllData(): LiveData<List<LectureModel>>

    @Query("SELECT * FROM lectures JOIN courses ON lectures.course_id = courses.id WHERE courses.course_name = :courseName")
    //"SELECT lecture_name FROM lectures INNER JOIN courses ON lectures.course_id = courses.id WHERE courses.id = lectures.course_id AND courses.course_name = :courseName"
    fun getLecturesByCourseName(courseName : String) : LiveData<List<LectureModel>>

    // SELECT lecture_name, courses.course_name
    // FROM lectures INNER JOIN courses ON lectures.course_id = courses.id
    // WHERE courses.id = lectures.course_id AND courses.course_name = "mEcHpAvTTNiPumm"

    @Update
    suspend fun updateLecture(lecture: LectureModel)

    @Delete
    suspend fun deleteLecture(lecture: LectureModel)
}