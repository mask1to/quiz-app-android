package com.example.quizappdiploma.fragments.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.MyDatabaseDao
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import com.example.quizappdiploma.fragments.viewmodels.helpers.LiveDataEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userDataRepository: UserDataRepository) : ViewModel()
{
    fun login(username: String, password: String) : LiveData<List<UserModel>>
    {
        return userDataRepository.getUserByUsernameAndPassword(username, password)
    }
}