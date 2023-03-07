package com.example.quizappdiploma.database.quizzes.answers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "quiz_answers")
class QuizAnswerModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    @ColumnInfo(name = "answer_name")
    val answerName : String?,
    @ColumnInfo(name = "is_answer_correct")
    val answerCorrect : Int?
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizAnswerModel

        if (id != other.id) return false
        if (answerName != other.answerName) return false
        if (answerCorrect != other.answerCorrect) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (answerName?.hashCode() ?: 0)
        result = 31 * result + (answerCorrect ?: 0)
        return result
    }

    override fun toString(): String {
        return "QuizAnswerModel(id=$id, answerName=$answerName, answerCorrect=$answerCorrect)"
    }
}