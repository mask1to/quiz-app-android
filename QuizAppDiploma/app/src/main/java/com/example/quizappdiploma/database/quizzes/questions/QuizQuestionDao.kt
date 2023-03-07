package com.example.quizappdiploma.database.quizzes.questions

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizappdiploma.database.lectures.LectureModel

@Dao
interface QuizQuestionDao
{
    @Query("SELECT * FROM quiz_questions WHERE question_difficulty <= 2 ORDER BY RANDOM() LIMIT 5")
    fun getFirstFiveQuestions(): LiveData<List<QuizQuestionModel>>
    @Query("SELECT * FROM quiz_questions WHERE question_difficulty >= 2 ORDER BY RANDOM() LIMIT 5")
    fun getLastFiveQuestions(): LiveData<List<QuizQuestionModel>>
    /*@Query("SELECT * FROM quiz_questions WHERE question_difficulty IN (:difficulties) LIMIT :limit")
    fun getQuestions(difficulties: List<Int>, limit: Int): LiveData<List<QuizQuestionModel>>*/

    @Query("SELECT * FROM quiz_questions WHERE id = :questionId")
    fun getQuestionById(questionId: Int): QuizQuestionModel?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuestion(question : QuizQuestionModel)
    @Update
    suspend fun updateQuestion(question: QuizQuestionModel)
    @Delete
    suspend fun deleteQuestion(question: QuizQuestionModel)

}