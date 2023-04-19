package com.example.quizappdiploma.database.quizzes.stats

class QuizStatsDataRepository(private val quizStatsDao: QuizStatsDao)
{

    suspend fun getAllStats(userId : Int): List<QuizStatsModel>
    {
        return quizStatsDao.getAllStats(userId)
    }

    suspend fun insertStats(quizStats: QuizStatsModel)
    {
        return quizStatsDao.insert(quizStats)
    }
}