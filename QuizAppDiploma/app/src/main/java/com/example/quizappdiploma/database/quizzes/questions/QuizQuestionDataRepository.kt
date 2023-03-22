package com.example.quizappdiploma.database.quizzes.questions

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.quizappdiploma.database.MyDatabase

class QuizQuestionDataRepository(private val quizQuestionDao: QuizQuestionDao)
{
    suspend fun addQuestion(question : QuizQuestionModel)
    {
        quizQuestionDao.addQuestion(question)
    }
    suspend fun updateQuestion(question: QuizQuestionModel)
    {
        quizQuestionDao.updateQuestion(question)
    }
    suspend fun deleteQuestion(question: QuizQuestionModel)
    {
        quizQuestionDao.deleteQuestion(question)
    }

    suspend fun updateQuestions(questions: List<QuizQuestionModel>)
    {
        quizQuestionDao.updateQuestions(questions)
    }

    fun getFirstFiveQuestions(courseId : Int, questionLimit : Int) : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDao.getFirstFiveQuestions(courseId, questionLimit)
    }

    fun getLastFiveQuestions(courseId: Int, questionDifficulty : Int, questionLimit : Int) : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDao.getLastFiveQuestions(courseId, questionDifficulty, questionLimit)
    }

    fun getImagePaths() : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDao.getImagePaths()
    }


}