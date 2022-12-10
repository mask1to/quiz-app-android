package com.example.quizappdiploma.fragments.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.fragments.viewmodels.LectureViewModel
import com.example.quizappdiploma.fragments.viewmodels.UserViewModel

class UserViewModelFactory(private val userDataRepository: UserDataRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(UserViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDataRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel entity class")
    }
}