package com.example.quizappdiploma.fragments.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.quizzes.answers.UserAnswersDataRepository
import com.example.quizappdiploma.fragments.viewmodels.UserAnswersViewModel

class UserAnswersViewModelFactory(private val userAnswersDataRepository: UserAnswersDataRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(UserAnswersViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return UserAnswersViewModel(userAnswersDataRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel entity class")
    }
}