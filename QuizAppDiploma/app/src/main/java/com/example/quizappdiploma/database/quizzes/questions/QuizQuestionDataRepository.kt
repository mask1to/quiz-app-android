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

    fun resetAllQuestions()
    {
        val allQuestions = quizQuestionDao.getAllQuestions()

        for(question in allQuestions)
        {
            question.alreadyUsed = 0
            quizQuestionDao.resetQuestion(question)
        }
    }

    fun updateQuestion(question: QuizQuestionModel)
    {
        quizQuestionDao.updateQuestion(question)
    }

    suspend fun deleteQuestion(question: QuizQuestionModel)
    {
        quizQuestionDao.deleteQuestion(question)
    }

    fun getFirstFiveQuestions(courseId : Int, questionLimit : Int) : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDao.getFirstFiveQuestions(courseId, questionLimit)
    }

    suspend fun getLastFiveQuestions(courseId: Int, questionDifficulty : Int, questionLimit : Int) : List<QuizQuestionModel>
    {
        return quizQuestionDao.getLastFiveQuestions(courseId, questionDifficulty, questionLimit)
    }

    suspend fun getAverageTimeSpentOnUsedQuestions(): Double?
    {
        return quizQuestionDao.getAverageTimeSpentOnUsedQuestions()
    }

}