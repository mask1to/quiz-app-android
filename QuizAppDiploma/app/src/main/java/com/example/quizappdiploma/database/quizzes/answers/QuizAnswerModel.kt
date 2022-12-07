package com.example.quizappdiploma.database.quizzes.answers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_answers")
class QuizAnswerModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "answer_name")
    val answerName : String,
    @ColumnInfo(name = "is_answer_correct")
    val answerCorrect : Boolean
)
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizAnswerModel

        if (id != other.id) return false
        if (answerName != other.answerName) return false
        if (answerCorrect != other.answerCorrect) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = id
        result = 31 * result + answerName.hashCode()
        result = 31 * result + answerCorrect.hashCode()
        return result
    }

    override fun toString(): String
    {
        return "QuizAnswer(id=$id, answerName='$answerName', answerCorrect=$answerCorrect)"
    }
}