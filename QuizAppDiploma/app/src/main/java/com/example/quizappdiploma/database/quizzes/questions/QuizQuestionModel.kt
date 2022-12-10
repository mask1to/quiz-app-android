package com.example.quizappdiploma.database.quizzes.questions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "quiz_questions")
class QuizQuestionModel(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    val id : Int,
    @ColumnInfo(name = "question_name")
    @NotNull
    val questionName : String,
    @ColumnInfo(name = "question_points")
    @NotNull
    val questionPoints : Int,
    @ColumnInfo(name = "question_difficulty")
    @NotNull
    val questionDifficulty : Int,
    @ColumnInfo(name = "is_question_multi_choice")
    @NotNull
    val questionMultiChoice : Int
)
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizQuestionModel

        if (id != other.id) return false
        if (questionName != other.questionName) return false
        if (questionPoints != other.questionPoints) return false
        if (questionDifficulty != other.questionDifficulty) return false
        if (questionMultiChoice != other.questionMultiChoice) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = id
        result = 31 * result + questionName.hashCode()
        result = 31 * result + questionPoints
        result = 31 * result + questionDifficulty
        result = 31 * result + questionMultiChoice.hashCode()
        return result
    }

    override fun toString(): String
    {
        return "QuizQuestion(id=$id, questionName='$questionName', questionPoints=$questionPoints, questionDifficulty=$questionDifficulty, questionMultiChoice=$questionMultiChoice)"
    }

}