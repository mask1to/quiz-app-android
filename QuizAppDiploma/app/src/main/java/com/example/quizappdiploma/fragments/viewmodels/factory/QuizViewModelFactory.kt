package com.example.quizappdiploma.fragments.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.quizzes.QuizDataRepository
import com.example.quizappdiploma.fragments.viewmodels.QuizViewModel

class QuizViewModelFactory(private val quizDataRepository: QuizDataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(QuizViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(quizDataRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel entity class")
    }
}