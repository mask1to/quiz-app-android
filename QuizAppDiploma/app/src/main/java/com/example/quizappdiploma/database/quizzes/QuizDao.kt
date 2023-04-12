package com.example.quizappdiploma.database.quizzes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuizDao
{
    @Query("SELECT id FROM quizzes WHERE course_id = :courseId")
    suspend fun getQuizIdByCourseId(courseId: Int): List<Int>

    @Query("SELECT id, course_id, quiz_name, total_points FROM quizzes WHERE course_id= :courseId")
    fun getAllQuizPropertiesByCourseId(courseId: Int) : LiveData<List<QuizModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizModel)

    @Update
    suspend fun updateQuiz(quiz: QuizModel)

    @Delete
    suspend fun deleteQuiz(quiz: QuizModel)
}