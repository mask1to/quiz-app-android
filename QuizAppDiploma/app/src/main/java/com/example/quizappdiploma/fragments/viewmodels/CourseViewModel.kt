package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.*
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel

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