package com.example.quizappdiploma.database.users

import com.example.quizappdiploma.database.MyLocalCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataRepository private constructor(private val cache : MyLocalCache)
{
    companion object{
        @Volatile
        private var INSTANCE : UserDataRepository? = null

        fun getInstance(cache: MyLocalCache) : UserDataRepository =
            INSTANCE ?: synchronized(this)
            {
                INSTANCE ?: UserDataRepository(cache).also { INSTANCE = it }
            }
    }

    suspend fun getUsers() : List<UserModel>
    {
        var userList = listOf<UserModel>()

        userList = userList.map {
            UserModel(it.id, it.email, it.username, it.password, it.isAdmin, it.isLecturer, it.isStudent)
        }
        withContext(Dispatchers.IO)
        {
            cache.deleteUsers()
            cache.insertUsers(userList)
        }


        return userList
    }

    suspend fun insertUser(userModel: UserModel)
    {
        cache.insertUser(userModel)
    }
}