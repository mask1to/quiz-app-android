package com.example.quizappdiploma.database.quizzes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "quizzes")
class QuizModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    @ColumnInfo(name = "quiz_name")
    val quizName : String?,
    @ColumnInfo(name = "time_begin")
    val quizTimeBegin : String?,
    @ColumnInfo(name = "time_end")
    val quizTimeEnd : String?,
    @ColumnInfo(name = "total_duration")
    val quizTotalTime: String?
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizModel

        if (id != other.id) return false
        if (quizName != other.quizName) return false
        if (quizTimeBegin != other.quizTimeBegin) return false
        if (quizTimeEnd != other.quizTimeEnd) return false
        if (quizTotalTime != other.quizTotalTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (quizName?.hashCode() ?: 0)
        result = 31 * result + (quizTimeBegin?.hashCode() ?: 0)
        result = 31 * result + (quizTimeEnd?.hashCode() ?: 0)
        result = 31 * result + (quizTotalTime?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "QuizModel(id=$id, quizName=$quizName, quizTimeBegin=$quizTimeBegin, quizTimeEnd=$quizTimeEnd, quizTotalTime=$quizTotalTime)"
    }
}