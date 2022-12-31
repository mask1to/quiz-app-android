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

class CourseViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<CourseModel>>
    private val repository: CourseDataRepository

    init {
        val courseDao = MyDatabase.getDatabase(application).courseDao()
        repository = CourseDataRepository(courseDao)
        readAllData = repository.readAllData
    }

    fun addCourse(course: CourseModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCourse(course)
        }
    }

    fun updateCourse(course: CourseModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCourse(course)
        }
    }

    fun deleteCourse(course: CourseModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCourse(course)
        }
    }

}