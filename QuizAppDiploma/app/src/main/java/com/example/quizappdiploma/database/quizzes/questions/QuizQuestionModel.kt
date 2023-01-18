package com.example.quizappdiploma.database.quizzes.questions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "quiz_questions")
class QuizQuestionModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "question_name")
    val questionName : String,
    @ColumnInfo(name = "image")
    val image : Int,
    @ColumnInfo(name = "question_points")
    val questionPoints : Int,
    @ColumnInfo(name = "question_difficulty")
    val questionDifficulty : Int,
    @ColumnInfo(name = "option_a")
    val questionOptionA : String,
    @ColumnInfo(name = "option_b")
    val questionOptionB : String,
    @ColumnInfo(name = "option_c")
    val questionOptionC : String,
    @ColumnInfo(name = "option_d")
    val questionOptionD : String,
    @ColumnInfo(name = "answer")
    val answer : Int
)
{

    override fun toString(): String {
        return "QuizQuestionModel(id=$id, questionName='$questionName', image=$image, questionPoints=$questionPoints, questionDifficulty=$questionDifficulty, questionOptionA='$questionOptionA', questionOptionB='$questionOptionB', questionOptionC='$questionOptionC', questionOptionD='$questionOptionD', answer='$answer')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizQuestionModel

        if (id != other.id) return false
        if (questionName != other.questionName) return false
        if (image != other.image) return false
        if (questionPoints != other.questionPoints) return false
        if (questionDifficulty != other.questionDifficulty) return false
        if (questionOptionA != other.questionOptionA) return false
        if (questionOptionB != other.questionOptionB) return false
        if (questionOptionC != other.questionOptionC) return false
        if (questionOptionD != other.questionOptionD) return false
        if (answer != other.answer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + questionName.hashCode()
        result = 31 * result + image
        result = 31 * result + questionPoints
        result = 31 * result + questionDifficulty
        result = 31 * result + questionOptionA.hashCode()
        result = 31 * result + questionOptionB.hashCode()
        result = 31 * result + questionOptionC.hashCode()
        result = 31 * result + questionOptionD.hashCode()
        result = 31 * result + answer
        return result
    }

}