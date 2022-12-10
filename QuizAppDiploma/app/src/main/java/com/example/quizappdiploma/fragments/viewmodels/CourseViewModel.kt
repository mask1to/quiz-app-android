package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.fragments.viewmodels.helpers.LiveDataEvent

class CourseViewModel(private val courseDataRepository: CourseDataRepository) : ViewModel()
{
    private val _message = MutableLiveData<LiveDataEvent<String>>()
    val message : LiveData<LiveDataEvent<String>> get() = _message
    val loadData = MutableLiveData(false)

    fun showMessage(message : String)
    {
        _message.postValue(LiveDataEvent(message))
    }

    fun insertCourses(course : CourseModel)
    {
        //courseDataRepository
    }

}