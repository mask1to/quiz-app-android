package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel

class QuizQuestionViewModel(private val quizQuestionDataRepository: QuizQuestionDataRepository): ViewModel()
{
    suspend fun addQuestion(question : QuizQuestionModel)
    {
        quizQuestionDataRepository.addQuestion(question)
    }

    suspend fun updateQuestion(question: QuizQuestionModel)
    {
        quizQuestionDataRepository.updateQuestion(question)
    }

    suspend fun deleteQuestion(question: QuizQuestionModel)
    {
        quizQuestionDataRepository.deleteQuestion(question)
    }

    fun getFirstFiveQuestions() : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDataRepository.getFirstFiveQuestions()
    }

    fun getLastFiveQuestions() : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDataRepository.getLastFiveQuestions()
    }
}