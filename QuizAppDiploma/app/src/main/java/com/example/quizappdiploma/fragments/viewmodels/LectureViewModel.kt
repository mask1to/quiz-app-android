package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.*
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.lectures.LectureModel
class LectureViewModel(private val lectureDataRepository: LectureDataRepository): ViewModel()
{
    fun getLecturesByCourseId(courseId: Int): LiveData<List<LectureModel>>
    {
        return lectureDataRepository.getLecturesByCourseId(courseId)
    }

}
