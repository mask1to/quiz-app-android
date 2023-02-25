package com.example.quizappdiploma.fragments.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.fragments.viewmodels.ContentViewModel
import com.example.quizappdiploma.fragments.viewmodels.LectureViewModel

class ContentViewModelFactory(private val lectureDataRepository: LectureDataRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(ContentViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return ContentViewModel(lectureDataRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel entity class")
    }
}