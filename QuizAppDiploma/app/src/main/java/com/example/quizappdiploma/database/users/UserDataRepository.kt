package com.example.quizappdiploma.database.users

import androidx.lifecycle.LiveData

class UserDataRepository(private val userDao: UserDao)
{
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserModel? {
        return userDao.getUserByEmailAndPassword(email, password)
    }

    fun getUsers() : LiveData<List<UserModel>>
    {
        return userDao.getUsers()
    }

    suspend fun insertUser(user: UserModel): Long {
        return userDao.insertUser(user)
    }
}