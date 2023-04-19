package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsDataRepository
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsModel
import kotlinx.coroutines.launch

class QuizStatsViewModel(private val repository: QuizStatsDataRepository) : ViewModel()
{
    suspend fun getAllStats(userId : Int): List<QuizStatsModel>
    {
        return repository.getAllStats(userId)
    }

    fun insertStats(quizStats: QuizStatsModel)
    {
        viewModelScope.launch {
            repository.insertStats(quizStats)
        }
    }
}