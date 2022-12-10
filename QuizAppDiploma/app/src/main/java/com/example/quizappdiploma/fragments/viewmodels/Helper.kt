package com.example.quizappdiploma.fragments.viewmodels

import android.content.Context
import android.service.autofill.UserData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.MyLocalCache
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.quizzes.QuizDataRepository
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.fragments.viewmodels.factory.CourseViewModelFactory
import com.example.quizappdiploma.fragments.viewmodels.factory.LectureViewModelFactory
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

    private fun getCourseRepository(context : Context) : CourseDataRepository
    {
        return CourseDataRepository.getInstance(allowCache(context))
    }

    private fun getLectureRepository(context : Context) : LectureDataRepository
    {
        return LectureDataRepository.getInstance(allowCache(context))
    }

    /*private fun getQuizRepository(context: Context) : QuizDataRepository
    {

    }*/

    fun getUserViewModelFactory(context: Context) : ViewModelProvider.Factory
    {
        return UserViewModelFactory(getUserRepository(context))
    }

    fun getCourseViewModelFactory(context: Context) : ViewModelProvider.Factory
    {
        return CourseViewModelFactory(getCourseRepository(context))
    }


    fun getLectureViewModelFactory(context: Context) : ViewModelProvider.Factory
    {
        return LectureViewModelFactory(getLectureRepository(context))
    }
}