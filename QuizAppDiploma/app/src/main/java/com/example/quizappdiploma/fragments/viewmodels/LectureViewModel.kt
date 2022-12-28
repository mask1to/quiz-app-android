package com.example.quizappdiploma.fragments.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.lectures.LectureModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LectureViewModel(application: Application): AndroidViewModel(application)
{
    val readAllData: LiveData<List<LectureModel>>
    private val repository: LectureDataRepository

    init {
        val lectureDao = MyDatabase.getDatabase(application).lectureDao()
        repository = LectureDataRepository(lectureDao)
        readAllData = repository.readAllData
    }

    fun addLecture(lecture : LectureModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLecture(lecture)
        }
    }

    fun updateLecture(lecture : LectureModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLecture(lecture)
        }
    }

    fun deleteLecture(lecture: LectureModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLecture(lecture)
        }
    }
}
