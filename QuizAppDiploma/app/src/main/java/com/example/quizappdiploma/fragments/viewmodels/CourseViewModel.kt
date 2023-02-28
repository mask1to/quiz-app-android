package com.example.quizappdiploma.fragments.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.MyDatabaseDao
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.fragments.viewmodels.helpers.LiveDataEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CourseViewModel(private val courseDataRepository: CourseDataRepository): ViewModel()
{
    fun getCoursesByIdAsc() : LiveData<List<CourseModel>>
    {
        return courseDataRepository.getCoursesByIdAsc()
    }

    suspend fun addCourse(course : CourseModel)
    {
        return courseDataRepository.addCourse(course)
    }

    suspend fun updateCourse(course : CourseModel)
    {
        return courseDataRepository.updateCourse(course)
    }

    suspend fun deleteCourse(course : CourseModel)
    {
        return courseDataRepository.deleteCourse(course)
    }
}