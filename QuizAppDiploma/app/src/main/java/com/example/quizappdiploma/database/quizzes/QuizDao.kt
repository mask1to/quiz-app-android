package com.example.quizappdiploma.database.quizzes

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuizDao
{
    @Query("SELECT id FROM quizzes WHERE course_id = :courseId")
    suspend fun getQuizIdByCourseId(courseId: Int): List<Int>
}