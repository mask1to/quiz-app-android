package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import com.example.quizappdiploma.fragments.viewmodels.helpers.LiveDataEvent

class UserViewModel(private val userDataRepository: UserDataRepository) : ViewModel()
{
    private val _message = MutableLiveData<LiveDataEvent<String>>()
    val message : LiveData<LiveDataEvent<String>> get() = _message
    val loadData = MutableLiveData(false)

    val users : LiveData<List<UserModel>> = liveData {
        loadData.postValue(true)
        val getUsers = userDataRepository.getUsers()
        emit(getUsers)
        loadData.postValue(false)
    }
}