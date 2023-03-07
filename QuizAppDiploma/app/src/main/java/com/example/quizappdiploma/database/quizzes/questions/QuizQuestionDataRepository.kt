package com.example.quizappdiploma.database.quizzes.questions

import androidx.lifecycle.LiveData
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

    fun getFirstFiveQuestions() : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDao.getFirstFiveQuestions()
    }

    fun getLastFiveQuestions() : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDao.getLastFiveQuestions()
    }
}