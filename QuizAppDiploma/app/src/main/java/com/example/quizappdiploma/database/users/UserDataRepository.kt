package com.example.quizappdiploma.database.users

import androidx.lifecycle.LiveData
import com.example.quizappdiploma.database.MyLocalCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataRepository private constructor(private val userDao: UserDao)
{
    fun getUserByUsernameAndPassword(username: String, password: String): LiveData<List<UserModel>>
    {
        return userDao.getUserByUsernameAndPassword(username, password)
    }
}