package com.example.quizappdiploma.database.quizzes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.quizappdiploma.database.courses.CourseModel
import org.jetbrains.annotations.NotNull

@Entity(tableName = "quizzes",
    foreignKeys = [ForeignKey(
    entity = CourseModel::class,
    childColumns = ["course_id"],
    parentColumns = ["id"]
)])
class QuizModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    @ColumnInfo(name = "course_id")
    val course_id : Int?,
    @ColumnInfo(name = "quiz_name")
    val quizName : String?,
    @ColumnInfo(name = "total_points")
    val quizTotalPoints : Int?
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizModel

        if (id != other.id) return false
        if (course_id != other.course_id) return false
        if (quizName != other.quizName) return false
        if (quizTotalPoints != other.quizTotalPoints) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (course_id ?: 0)
        result = 31 * result + (quizName?.hashCode() ?: 0)
        result = 31 * result + (quizTotalPoints ?: 0)
        return result
    }

    override fun toString(): String {
        return "QuizModel(id=$id, course_id=$course_id, quizName=$quizName, quizTotalPoints=$quizTotalPoints)"
    }
}