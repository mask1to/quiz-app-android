package com.example.quizappdiploma.database.quizzes.stats

class QuizStatsDataRepository(private val quizStatsDao: QuizStatsDao)
{

    suspend fun getAllStats(): List<QuizStatsModel> {
        return quizStatsDao.getAllStats()
    }

    suspend fun insert(quizStats: QuizStatsModel) {
        quizStatsDao.insert(quizStats)
    }
}