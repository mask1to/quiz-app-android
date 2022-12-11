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
    private val _message = MutableLiveData<LiveDataEvent<String>>()
    val message : LiveData<LiveDataEvent<String>> get() = _message
    val loadData = MutableLiveData(false)

    val users : LiveData<List<UserModel>> = liveData {
        loadData.postValue(true)
        val getUsers = userDataRepository.getUsers()
        emit(getUsers)
        loadData.postValue(false)
    }


    fun insertUser(user : UserModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.insertUser(user)
        }
    }
}