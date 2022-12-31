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

    suspend fun insertUser(userModel: UserModel)
    {
        cache.insertUser(userModel)
    }
}