package com.example.quizappdiploma.database.courses

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CourseDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCourse(course : CourseModel)
    @Query("SELECT * FROM courses ORDER BY id ASC")
    fun getCoursesOrderByIdAsc(): LiveData<List<CourseModel>>
    @Query("SELECT * FROM courses WHERE course_name= :courseName")
    fun getCourseByName(courseName : String) : LiveData<List<CourseModel>>
    @Query("SELECT id, course_name FROM courses")
    fun getCourseNames() : LiveData<List<CourseModel>>

    @Query("SELECT id FROM courses WHERE course_name= :courseName")
    fun getCourseIdByName(courseName : String) : LiveData<List<CourseModel>>
    @Update
    suspend fun updateCourse(course: CourseModel)
    @Delete
    suspend fun deleteCourse(course: CourseModel)
}