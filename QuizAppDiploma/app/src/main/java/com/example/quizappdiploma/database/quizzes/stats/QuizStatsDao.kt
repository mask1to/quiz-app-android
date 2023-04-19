package com.example.quizappdiploma.database.quizzes.stats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizStatsDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(quizStats: QuizStatsModel)

    @Query("SELECT * FROM quiz_stats WHERE user_id=:userId")
    suspend fun getAllStats(userId : Int): List<QuizStatsModel>
}