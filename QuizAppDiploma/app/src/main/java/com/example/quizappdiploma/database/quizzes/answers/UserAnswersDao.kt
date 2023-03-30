package com.example.quizappdiploma.database.quizzes.answers

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserAnswersDao
{
    @Insert
    suspend fun addUserAnswer(userAnswer: UserAnswers)

    @Update
    suspend fun updateUserAnswer(userAnswer: UserAnswers)

    @Query("SELECT * FROM user_answers WHERE user_id = :userId AND quiz_id = :quizId")
    suspend fun getUserAnswersByQuiz(userId: Int, quizId: Int): List<UserAnswers>

    @Query("SELECT * FROM user_answers WHERE user_id = :userId")
    suspend fun getAllUserAnswers(userId: Int): List<UserAnswers>

    @Query("SELECT * FROM user_answers WHERE user_id = :userId AND question_id = :questionId")
    suspend fun getUserAnswerByQuestion(userId: Int, questionId: Int): UserAnswers?

    @Query("DELETE FROM user_answers WHERE user_id = :userId AND quiz_id = :quizId")
    suspend fun deleteUserAnswersByQuiz(userId: Int, quizId: Int)
}