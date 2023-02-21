package com.example.quizappdiploma.fragments.viewmodels

import android.app.Application
import androidx.lifecycle.*
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
    var selectedLectures : LiveData<List<LectureModel>?>? = null
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

    fun getLecturesByCourseName(courseName : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLecturesByCourseName(courseName)
        }
    }

    fun getLecturesByCourseId(courseId : Int)
    {
        /*viewModelScope.launch(Dispatchers.IO){
            repository.getLecturesByCourseId(courseId)
        }*/

        selectedLectures = liveData {
            repository.getLecturesByCourseId(courseId)
            //emitSource(repository.getLecturesByCourseId(courseId))
        }
    }
}
