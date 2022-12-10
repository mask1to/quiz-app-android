package com.example.quizappdiploma.database.quizzes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "quizzes")
class QuizModel(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    val id : Int,
    @ColumnInfo(name = "quiz_name")
    @NotNull
    val quizName : String,
    @ColumnInfo(name = "is_active")
    @NotNull
    val isQuizActive : Int,
    @ColumnInfo(name = "quiz_difficulty")
    @NotNull
    val quizDifficulty : Int
)
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizModel

        if (id != other.id) return false
        if (quizName != other.quizName) return false
        if (isQuizActive != other.isQuizActive) return false
        if (quizDifficulty != other.quizDifficulty) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = id
        result = 31 * result + quizName.hashCode()
        result = 31 * result + isQuizActive.hashCode()
        result = 31 * result + quizDifficulty
        return result
    }

    override fun toString(): String
    {
        return "Quiz(id=$id, quizName='$quizName', isQuizActive=$isQuizActive, quizDifficulty=$quizDifficulty)"
    }

}