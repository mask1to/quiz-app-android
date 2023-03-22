package com.example.quizappdiploma.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.database.users.UserModel

@Dao
interface MyDatabaseDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourses(courses : List<CourseModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLectures(lectures : List<LectureModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userModel: UserModel)

    @Update
    suspend fun updateUser(userModel: UserModel)

    @Query("DELETE FROM courses")
    fun deleteCourses()

    @Query("DELETE FROM lectures")
    fun deleteLectures()

    @Query("DELETE FROM users")
    fun deleteUsers()

    @Query("DELETE FROM quizzes")
    suspend fun deleteQuizzes()

    @Query("DELETE FROM quiz_questions")
    suspend fun deleteQuizQuestions()

    @Query("SELECT * FROM courses ORDER BY id ASC")
    fun readAllData() : LiveData<List<CourseModel>>


}