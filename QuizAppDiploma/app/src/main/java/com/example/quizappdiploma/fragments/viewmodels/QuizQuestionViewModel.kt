package com.example.quizappdiploma.fragments.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizQuestionViewModel(application: Application): AndroidViewModel(application)
{
    val readAllData: LiveData<List<QuizQuestionModel>>
    private val repository : QuizQuestionDataRepository

    init {
        val quizQuestionDao = MyDatabase.getDatabase(application).quizQuestionDao()
        repository = QuizQuestionDataRepository(quizQuestionDao)
        readAllData = repository.readAllData
    }

    fun addQuestion(question : QuizQuestionModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addQuestion(question)
        }
    }

    fun updateQuestion(question: QuizQuestionModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateQuestion(question)
        }
    }

    fun deleteQuestion(question: QuizQuestionModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQuestion(question)
        }
    }
}