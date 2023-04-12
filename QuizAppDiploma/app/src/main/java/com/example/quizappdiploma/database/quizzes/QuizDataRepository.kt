package com.example.quizappdiploma.database.quizzes

import androidx.lifecycle.LiveData

class QuizDataRepository(private val quizDao : QuizDao)
{

    fun getAllQuizPropertiesByCourseId(courseId: Int) : LiveData<List<QuizModel>>
    {
        return quizDao.getAllQuizPropertiesByCourseId(courseId)
    }
    suspend fun getQuizIdByCourseId(courseId: Int): List<Int>
    {
        return quizDao.getQuizIdByCourseId(courseId)
    }

    suspend fun insertQuiz(quiz: QuizModel)
    {
        return quizDao.insertQuiz(quiz)
    }

    suspend fun updateQuiz(quiz: QuizModel)
    {
        return quizDao.updateQuiz(quiz)
    }

    suspend fun deleteQuiz(quiz: QuizModel)
    {
        return quizDao.deleteQuiz(quiz)
    }
}