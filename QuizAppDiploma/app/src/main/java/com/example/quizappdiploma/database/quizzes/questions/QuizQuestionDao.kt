package com.example.quizappdiploma.database.quizzes.questions

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizappdiploma.database.lectures.LectureModel

@Dao
interface QuizQuestionDao
{
    @Query("SELECT * FROM quiz_questions")
    fun getAllQuestions(): LiveData<List<QuizQuestionModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuestion(question : QuizQuestionModel)

    @Update
    suspend fun updateQuestion(question: QuizQuestionModel)

    @Delete
    suspend fun deleteQuestion(question: QuizQuestionModel)

}