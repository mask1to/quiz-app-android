package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizappdiploma.database.quizzes.QuizDataRepository
import kotlinx.coroutines.launch

class QuizViewModel(private val quizDataRepository: QuizDataRepository) : ViewModel()
{
    fun getQuizIdByCourseId(courseId: Int, onResult: (List<Int>) -> Unit) {
        viewModelScope.launch {
            val result = quizDataRepository.getQuizIdByCourseId(courseId)
            onResult(result)
        }
    }
}