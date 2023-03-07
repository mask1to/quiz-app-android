package com.example.quizappdiploma.fragments.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.fragments.viewmodels.QuizQuestionViewModel
class QuizQuestionViewModelFactory(private val quizQuestionDataRepository: QuizQuestionDataRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(QuizQuestionViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return QuizQuestionViewModel(quizQuestionDataRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel entity class")
    }
}