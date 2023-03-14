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
    fun getFirstFiveQuestions(courseId : Int, questionLimit: Int) : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDataRepository.getFirstFiveQuestions(courseId, questionLimit)
    }
    fun getLastFiveQuestions(courseId: Int, questionDifficulty : Int, questionLimit : Int) : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDataRepository.getLastFiveQuestions(courseId, questionDifficulty, questionLimit)
    }
}