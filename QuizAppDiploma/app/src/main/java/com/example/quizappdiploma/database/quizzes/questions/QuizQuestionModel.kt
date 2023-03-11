package com.example.quizappdiploma.database.quizzes.questions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.quizappdiploma.database.courses.CourseModel
import org.jetbrains.annotations.NotNull

@Entity(tableName = "quiz_questions",
    foreignKeys = [ForeignKey(
        entity = CourseModel::class,
        childColumns = ["course_id"],
        parentColumns = ["id"]
    )])
class QuizQuestionModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    @ColumnInfo(name = "course_id")
    val courseId : Int?,
    @ColumnInfo(name = "question_name")
    val questionName : String?,
    @ColumnInfo(name = "image_path")
    val image_path : String?,
    @ColumnInfo(name = "question_points")
    val questionPoints : Int?,
    @ColumnInfo(name = "question_difficulty")
    val questionDifficulty : Int?,
    @ColumnInfo(name = "option_a")
    val questionOptionA : String?,
    @ColumnInfo(name = "option_b")
    val questionOptionB : String?,
    @ColumnInfo(name = "option_c")
    val questionOptionC : String?,
    @ColumnInfo(name = "option_d")
    val questionOptionD : String?,
    @ColumnInfo(name = "answer")
    val answer : Int?
)
{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizQuestionModel

        if (id != other.id) return false
        if (courseId != other.courseId) return false
        if (questionName != other.questionName) return false
        if (image_path != other.image_path) return false
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
        var result = id ?: 0
        result = 31 * result + (courseId ?: 0)
        result = 31 * result + (questionName?.hashCode() ?: 0)
        result = 31 * result + (image_path?.hashCode() ?: 0)
        result = 31 * result + (questionPoints ?: 0)
        result = 31 * result + (questionDifficulty ?: 0)
        result = 31 * result + (questionOptionA?.hashCode() ?: 0)
        result = 31 * result + (questionOptionB?.hashCode() ?: 0)
        result = 31 * result + (questionOptionC?.hashCode() ?: 0)
        result = 31 * result + (questionOptionD?.hashCode() ?: 0)
        result = 31 * result + (answer ?: 0)
        return result
    }

    override fun toString(): String {
        return "QuizQuestionModel(id=$id, courseId=$courseId, questionName=$questionName, image_path=$image_path, questionPoints=$questionPoints, questionDifficulty=$questionDifficulty, questionOptionA=$questionOptionA, questionOptionB=$questionOptionB, questionOptionC=$questionOptionC, questionOptionD=$questionOptionD, answer=$answer)"
    }
}