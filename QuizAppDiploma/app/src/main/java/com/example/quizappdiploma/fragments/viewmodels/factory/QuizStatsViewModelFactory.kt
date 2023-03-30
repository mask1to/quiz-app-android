package com.example.quizappdiploma.fragments.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsDataRepository
import com.example.quizappdiploma.fragments.viewmodels.QuizStatsViewModel

class QuizStatsViewModelFactory (private val quizStatsDataRepository: QuizStatsDataRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(QuizStatsViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return QuizStatsViewModel(quizStatsDataRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel entity class")
    }
}