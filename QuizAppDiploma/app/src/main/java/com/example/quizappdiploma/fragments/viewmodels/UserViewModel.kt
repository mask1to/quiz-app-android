package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.*
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import kotlinx.coroutines.launch

class UserViewModel(private val userDataRepository: UserDataRepository) : ViewModel()
{
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserModel?
    {
        return userDataRepository.getUserByEmailAndPassword(email, password)
    }
    fun getUsers() : LiveData<List<UserModel>>
    {
        return userDataRepository.getUsers()
    }

    fun getUserByUsernameAndEmail(username: String, email: String): LiveData<List<UserModel>>
    {
        return userDataRepository.getUserByUsernameAndEmail(username, email)
    }

    fun getUserByEmail(email : String) : LiveData<List<UserModel>>
    {
        return userDataRepository.getUserByEmail(email)
    }
    fun insertUser(user: UserModel)
    {
        viewModelScope.launch {
            userDataRepository.insertUser(user)
        }
    }
    fun updateUser(user: UserModel)
    {
        viewModelScope.launch {
            userDataRepository.updateUser(user)
        }
    }
    fun deleteUser(user : UserModel)
    {
        viewModelScope.launch {
            userDataRepository.deleteUser(user)
        }
    }
}