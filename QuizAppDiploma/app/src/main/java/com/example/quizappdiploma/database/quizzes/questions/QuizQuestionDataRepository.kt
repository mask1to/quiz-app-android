package com.example.quizappdiploma.database.quizzes.questions

import androidx.lifecycle.LiveData

class QuizQuestionDataRepository(private val quizQuestionDao: QuizQuestionDao)
{
    val readAllData : LiveData<List<QuizQuestionModel>> = quizQuestionDao.getAllQuestions()

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
}