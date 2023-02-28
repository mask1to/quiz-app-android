package com.example.quizappdiploma.fragments.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.fragments.viewmodels.CourseViewModel

class CourseViewModelFactory(private val courseDataRepository: CourseDataRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(CourseViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(courseDataRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel entity class")
    }
}