package com.example.quizappdiploma.fragments.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.lectures.LectureModel
class ContentViewModel(private val lectureDataRepository: LectureDataRepository) : ViewModel()
{
    fun getLectureImagePaths() : LiveData<List<LectureModel>>
    {
        return lectureDataRepository.getLectureImagePaths()
    }
}