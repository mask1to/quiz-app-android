package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizQuestionViewModel(private val quizQuestionDataRepository: QuizQuestionDataRepository): ViewModel()
{
    fun addQuestion(question : QuizQuestionModel)
    {
        viewModelScope.launch {
            quizQuestionDataRepository.addQuestion(question)
        }
    }

    fun updateWholeQuestion(question: QuizQuestionModel)
    {
        viewModelScope.launch {
            quizQuestionDataRepository.updateWholeQuestion(question)
        }
    }
    fun updateQuestion(question: QuizQuestionModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            question.alreadyUsed = 1
            quizQuestionDataRepository.updateQuestion(question)
        }
    }
    fun resetAllQuestions()
    {
        viewModelScope.launch(Dispatchers.IO) {
            quizQuestionDataRepository.resetAllQuestions()
        }
    }

    fun getQuestionNamesByCourseId(courseId: Int) : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDataRepository.getQuestionNamesByCourseId(courseId)
    }

    fun getQuestionPropsByQuestionName(questionName: String) : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDataRepository.getQuestionPropsByQuestionName(questionName)
    }
    suspend fun deleteQuestion(question: QuizQuestionModel)
    {
        quizQuestionDataRepository.deleteQuestion(question)
    }
    fun getFirstFiveQuestions(courseId : Int, questionLimit: Int) : LiveData<List<QuizQuestionModel>>
    {
        return quizQuestionDataRepository.getFirstFiveQuestions(courseId, questionLimit)
    }
    suspend fun getLastFiveQuestions(courseId: Int, questionDifficulty : Int, questionLimit : Int) : List<QuizQuestionModel>
    {
        return quizQuestionDataRepository.getLastFiveQuestions(courseId, questionDifficulty, questionLimit)
    }

    suspend fun getAverageTimeSpentOnUsedQuestions(): Double?
    {
        return quizQuestionDataRepository.getAverageTimeSpentOnUsedQuestions()
    }

}