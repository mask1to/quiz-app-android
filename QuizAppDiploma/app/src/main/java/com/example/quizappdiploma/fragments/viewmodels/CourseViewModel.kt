package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.*
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import kotlinx.coroutines.launch

class CourseViewModel(private val courseDataRepository: CourseDataRepository): ViewModel()
{
    fun getCoursesByIdAsc() : LiveData<List<CourseModel>>
    {
        return courseDataRepository.getCoursesByIdAsc()
    }
    fun getCourseByName(courseName : String) : LiveData<List<CourseModel>>
    {
        return courseDataRepository.getCourseByName(courseName)
    }

    fun getCourseNames() : LiveData<List<CourseModel>>
    {
        return courseDataRepository.getCourseNames()
    }

    fun getCourseIdByName(courseName : String) : LiveData<List<CourseModel>>
    {
        return courseDataRepository.getCourseIdByName(courseName)
    }
    fun addCourse(course : CourseModel)
    {
        viewModelScope.launch {
            courseDataRepository.addCourse(course)
        }
    }
    fun updateCourse(course : CourseModel)
    {
        viewModelScope.launch{
            courseDataRepository.updateCourse(course)
        }
    }
    fun deleteCourse(course : CourseModel)
    {
        viewModelScope.launch {
            courseDataRepository.deleteCourse(course)
        }
    }
}