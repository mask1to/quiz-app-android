package com.example.quizappdiploma.database.courses

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CourseDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCourse(course : CourseModel)

    @Query("SELECT * FROM courses ORDER BY id ASC")
    fun readAllData(): LiveData<List<CourseModel>>
}