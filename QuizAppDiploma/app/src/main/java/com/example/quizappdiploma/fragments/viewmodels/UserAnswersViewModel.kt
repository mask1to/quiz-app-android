package com.example.quizappdiploma.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizappdiploma.database.quizzes.answers.UserAnswers
import com.example.quizappdiploma.database.quizzes.answers.UserAnswersDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class UserAnswersViewModel(private val userAnswersRepository: UserAnswersDataRepository) : ViewModel() {

    fun addUserAnswer(userAnswer: UserAnswers) = viewModelScope.launch(Dispatchers.IO) {
        userAnswersRepository.addUserAnswer(userAnswer)
    }

    fun updateUserAnswer(userAnswer: UserAnswers) = viewModelScope.launch(Dispatchers.IO) {
        userAnswersRepository.updateUserAnswer(userAnswer)
    }

    fun getUserAnswersByQuiz(userId: Int, quizId: Int) = viewModelScope.launch(Dispatchers.IO) {
        userAnswersRepository.getUserAnswersByQuiz(userId, quizId)
    }

    fun getAllUserAnswers(userId: Int) = viewModelScope.launch(Dispatchers.IO) {
        userAnswersRepository.getAllUserAnswers(userId)
    }

    fun getUserAnswerByQuestion(userId: Int, questionId: Int) = viewModelScope.launch(Dispatchers.IO) {
        userAnswersRepository.getUserAnswerByQuestion(userId, questionId)
    }

    fun deleteUserAnswersByQuiz(userId: Int, quizId: Int) = viewModelScope.launch(Dispatchers.IO) {
        userAnswersRepository.deleteUserAnswersByQuiz(userId, quizId)
    }
}