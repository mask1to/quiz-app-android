package com.example.quizappdiploma.database.quizzes.questions

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizappdiploma.database.lectures.LectureModel

@Dao
interface QuizQuestionDao
{
    @Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND question_difficulty <= 2 ORDER BY RANDOM() LIMIT :questionLimit")
    fun getFirstFiveQuestions(courseId: Int, questionLimit: Int): LiveData<List<QuizQuestionModel>>
    /*@Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND question_difficulty = :questionDifficulty AND alreadyUsed != 1 ORDER BY RANDOM() LIMIT :questionLimit")
    fun getLastFiveQuestions(courseId: Int, questionDifficulty: Int, questionLimit: Int): LiveData<List<QuizQuestionModel>>*/
    @Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND question_difficulty = :questionDifficulty AND alreadyUsed != 1 ORDER BY RANDOM() LIMIT :questionLimit")
    suspend fun getLastFiveQuestions(courseId: Int, questionDifficulty: Int, questionLimit: Int): List<QuizQuestionModel>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuestion(question : QuizQuestionModel)
    @Update
    fun updateQuestion(question: QuizQuestionModel)
    @Delete
    suspend fun deleteQuestion(question: QuizQuestionModel)

}