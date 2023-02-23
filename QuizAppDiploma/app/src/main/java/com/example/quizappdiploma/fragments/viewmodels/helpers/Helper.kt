package com.example.quizappdiploma.fragments.viewmodels.helpers

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.MyLocalCache
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.fragments.viewmodels.factory.UserViewModelFactory

object Helper
{
    private fun allowCache(context : Context) : MyLocalCache
    {
        val db = MyDatabase.getDatabase(context)
        return MyLocalCache(db.myDatabaseDao())
    }
    private fun getUserRepository(context: Context) : UserDataRepository
    {
        return UserDataRepository.getInstance(allowCache(context))
    }
    fun getUserViewModelFactory(context: Context) : ViewModelProvider.Factory
    {
        return UserViewModelFactory(getUserRepository(context))
    }

}