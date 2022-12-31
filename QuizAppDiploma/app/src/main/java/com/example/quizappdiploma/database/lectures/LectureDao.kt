package com.example.quizappdiploma.database.lectures

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LectureDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLecture(lecture : LectureModel)

    @Query("SELECT * FROM lectures ORDER BY id ASC")
    fun readAllData(): LiveData<List<LectureModel>>

    @Update
    suspend fun updateLecture(lecture: LectureModel)

    @Delete
    suspend fun deleteLecture(lecture: LectureModel)
}