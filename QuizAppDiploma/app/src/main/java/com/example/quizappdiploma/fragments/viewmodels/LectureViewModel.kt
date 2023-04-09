package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.*
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.lectures.LectureModel
import kotlinx.coroutines.launch

class LectureViewModel(private val lectureDataRepository: LectureDataRepository): ViewModel()
{
    fun getLecturesByCourseId(courseId: Int): LiveData<List<LectureModel>>
    {
        return lectureDataRepository.getLecturesByCourseId(courseId)
    }

    fun getLectureNameByCourseId(courseId : Int) : LiveData<List<LectureModel>>
    {
        return lectureDataRepository.getLectureNameByCourseId(courseId)
    }

    fun getLectureDescByLectureName(lectureName : String) : LiveData<List<LectureModel>>
    {
        return lectureDataRepository.getLectureDescByLectureName(lectureName)
    }

    fun addLecture(lecture : LectureModel)
    {
        viewModelScope.launch {
            lectureDataRepository.addLecture(lecture)
        }
    }

    fun updateLecture(lecture: LectureModel)
    {
        viewModelScope.launch {
            lectureDataRepository.updateLecture(lecture)
        }
    }

    fun deleteLecture(lecture : LectureModel)
    {
        viewModelScope.launch {
            lectureDataRepository.deleteLecture(lecture)
        }
    }

}
