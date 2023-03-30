package com.example.quizappdiploma.database.quizzes.answers

class UserAnswersDataRepository(private val userAnswersDao: UserAnswersDao)
{
    suspend fun addUserAnswer(userAnswer: UserAnswers){
        return userAnswersDao.addUserAnswer(userAnswer)
    }

    suspend fun updateUserAnswer(userAnswer: UserAnswers) {
        userAnswersDao.updateUserAnswer(userAnswer)
    }

    suspend fun getUserAnswersByQuiz(userId: Int, quizId: Int): List<UserAnswers> {
        return userAnswersDao.getUserAnswersByQuiz(userId, quizId)
    }

    suspend fun getAllUserAnswers(userId: Int): List<UserAnswers> {
        return userAnswersDao.getAllUserAnswers(userId)
    }

    suspend fun getUserAnswerByQuestion(userId: Int, questionId: Int): UserAnswers? {
        return userAnswersDao.getUserAnswerByQuestion(userId, questionId)
    }

    suspend fun deleteUserAnswersByQuiz(userId: Int, quizId: Int) {
        userAnswersDao.deleteUserAnswersByQuiz(userId, quizId)
    }
}