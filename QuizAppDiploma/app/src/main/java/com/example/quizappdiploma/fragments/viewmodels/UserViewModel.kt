package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.*
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel

class UserViewModel(private val userDataRepository: UserDataRepository) : ViewModel()
{
    fun login(username: String, password: String) : LiveData<List<UserModel>>
    {
        return userDataRepository.getUserByUsernameAndPassword(username, password)
    }

    fun getUsers() : LiveData<List<UserModel>>
    {
        return userDataRepository.getUsers()
    }
}