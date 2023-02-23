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

class LectureViewModel(private val lectureDataRepository: LectureDataRepository): ViewModel()
{

    fun getLecturesByCourseId(courseId: Int): LiveData<List<LectureModel>>
    {
        return lectureDataRepository.getLecturesByCourseId(courseId)
    }

}
