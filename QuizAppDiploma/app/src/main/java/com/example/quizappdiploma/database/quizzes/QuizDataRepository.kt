package com.example.quizappdiploma.database.quizzes

class QuizDataRepository(private val quizDao : QuizDao)
{
    suspend fun getQuizIdByCourseId(courseId: Int): List<Int>
    {
        return quizDao.getQuizIdByCourseId(courseId)
    }
}