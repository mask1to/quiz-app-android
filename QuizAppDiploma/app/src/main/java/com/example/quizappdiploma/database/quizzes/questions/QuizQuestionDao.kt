package com.example.quizappdiploma.database.quizzes.questions

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizappdiploma.database.lectures.LectureModel

@Dao
interface QuizQuestionDao
{
    @Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND question_difficulty <= 2 ORDER BY RANDOM() LIMIT :questionLimit")
    fun getFirstFiveQuestions(courseId: Int, questionLimit: Int): LiveData<List<QuizQuestionModel>>
    /*@Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND question_difficulty >= 2 ORDER BY RANDOM() LIMIT 5")
    fun getLastFiveQuestions(courseId: Int): LiveData<List<QuizQuestionModel>>*/

    @Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND question_difficulty = :questionDifficulty ORDER BY RANDOM() LIMIT :questionLimit")
    fun getLastFiveQuestions(courseId: Int, questionDifficulty : Int, questionLimit : Int) : LiveData<List<QuizQuestionModel>>

    /*
    @Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND (question_difficulty = 1 OR question_difficulty = 2 OR question_difficulty = 3) ORDER BY question_difficulty DESC, RANDOM() LIMIT 5")
    fun getLastFiveQuestions(courseId: Int): LiveData<List<QuizQuestionModel>>*/
    @Query("SELECT * FROM quiz_questions WHERE id = :questionId")
    fun getQuestionById(questionId: Int): QuizQuestionModel?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuestion(question : QuizQuestionModel)
    @Update
    suspend fun updateQuestion(question: QuizQuestionModel)
    @Delete
    suspend fun deleteQuestion(question: QuizQuestionModel)

}