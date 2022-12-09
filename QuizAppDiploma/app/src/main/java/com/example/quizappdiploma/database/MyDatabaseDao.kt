package com.example.quizappdiploma.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureModel

@Dao
interface MyDatabaseDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses : List<CourseModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectures(lectures : List<LectureModel>)

    @Query("DELETE FROM courses")
    suspend fun deleteCourses()

    @Query("DELETE FROM lectures")
    suspend fun deleteLectures()

    @Query("DELETE FROM quizzes")
    suspend fun deleteQuizzes()

    @Query("DELETE FROM quiz_answers")
    suspend fun deleteQuizAnswers()

    @Query("DELETE FROM quiz_questions")
    suspend fun deleteQuizQuestions()

    /*
    @Query("SELECT * FROM courses ORDER BY course_name ASC")
    suspend fun getCourses()

    @Query("SELECT * FROM lectures ORDER BY lecture_name ASC")
    suspend fun getLectures()*/
}