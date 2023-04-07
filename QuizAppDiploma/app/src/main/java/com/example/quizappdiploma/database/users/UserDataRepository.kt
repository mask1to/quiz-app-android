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

    fun getUserByUsernameAndEmail(username : String, email: String): LiveData<List<UserModel>>
    {
        return userDao.getUserByUsernameAndEmail(username, email)
    }

    fun getUserByEmail(email : String) : LiveData<List<UserModel>>
    {
        return userDao.getUserByEmail(email)
    }
    suspend fun insertUser(user: UserModel){
        return userDao.insertUser(user)
    }
    suspend fun updateUser(user: UserModel)
    {
        userDao.updateUser(user)
    }
    suspend fun deleteUser(user : UserModel)
    {
        return userDao.deleteUser(user)
    }
}