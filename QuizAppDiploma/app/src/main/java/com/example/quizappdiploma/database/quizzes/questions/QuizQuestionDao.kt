package com.example.quizappdiploma.database.quizzes.questions

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizappdiploma.database.lectures.LectureModel

@Dao
interface QuizQuestionDao
{
    @Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND question_difficulty <= 2 ORDER BY RANDOM() LIMIT :questionLimit")
    fun getFirstFiveQuestions(courseId: Int, questionLimit: Int): LiveData<List<QuizQuestionModel>>
    @Query("SELECT * FROM quiz_questions WHERE course_id = :courseId AND question_difficulty = :questionDifficulty AND alreadyUsed == 0 ORDER BY RANDOM() LIMIT :questionLimit")
    suspend fun getLastFiveQuestions(courseId: Int, questionDifficulty: Int, questionLimit: Int): List<QuizQuestionModel>
    @Query
        ("""
            SELECT AVG(time_spent) FROM 
            (
                SELECT time_spent FROM user_answers WHERE question_id IN 
                (
                    SELECT id FROM quiz_questions WHERE alreadyUsed = 1
                )
                ORDER BY id DESC LIMIT 5
            ) AS last_five_answers
        """)
    suspend fun getAverageTimeSpentOnUsedQuestions(): Double?

    @Query("SELECT * FROM quiz_questions")
    fun getAllQuestions() : List<QuizQuestionModel>

    @Query("SELECT * FROM quiz_questions WHERE course_id=:courseId")
    fun getQuestionNamesByCourseId(courseId: Int) : LiveData<List<QuizQuestionModel>>

    @Query("SELECT * FROM quiz_questions WHERE question_name=:questionName")
    fun getQuestionPropsByQuestionName(questionName: String) : LiveData<List<QuizQuestionModel>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuestion(question : QuizQuestionModel)

    @Update
    fun updateQuestion(question: QuizQuestionModel)

    @Update
    fun resetQuestion(question: QuizQuestionModel)

    @Update
    suspend fun updateWholeQuestion(question: QuizQuestionModel)

    @Delete
    suspend fun deleteQuestion(question: QuizQuestionModel)

}