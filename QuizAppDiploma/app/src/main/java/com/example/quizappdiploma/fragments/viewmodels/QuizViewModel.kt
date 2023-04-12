package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizappdiploma.database.quizzes.QuizDataRepository
import com.example.quizappdiploma.database.quizzes.QuizModel
import kotlinx.coroutines.launch

class QuizViewModel(private val quizDataRepository: QuizDataRepository) : ViewModel()
{
    fun getQuizIdByCourseId(courseId: Int, onResult: (List<Int>) -> Unit) {
        viewModelScope.launch {
            val result = quizDataRepository.getQuizIdByCourseId(courseId)
            onResult(result)
        }
    }

    fun getAllQuizPropertiesByCourseId(courseId: Int) : LiveData<List<QuizModel>>
    {
        return quizDataRepository.getAllQuizPropertiesByCourseId(courseId)
    }

    fun insertQuiz(quiz: QuizModel)
    {
        viewModelScope.launch {
            quizDataRepository.insertQuiz(quiz)
        }
    }

    fun updateQuiz(quiz: QuizModel)
    {
        viewModelScope.launch {
            quizDataRepository.updateQuiz(quiz)
        }
    }

    fun deleteQuiz(quiz: QuizModel)
    {
        viewModelScope.launch {
            quizDataRepository.deleteQuiz(quiz)
        }
    }
}