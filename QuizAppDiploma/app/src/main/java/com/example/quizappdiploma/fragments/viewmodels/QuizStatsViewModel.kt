package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsDataRepository
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsModel
import kotlinx.coroutines.launch

class QuizStatsViewModel(private val repository: QuizStatsDataRepository) : ViewModel()
{

    fun getAllStats(callback: (List<QuizStatsModel>) -> Unit) {
        viewModelScope.launch {
            val stats = repository.getAllStats()
            callback(stats)
        }
    }

    fun insert(quizStats: QuizStatsModel) {
        viewModelScope.launch {
            repository.insert(quizStats)
        }
    }
}