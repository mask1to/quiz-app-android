package com.example.quizappdiploma.database.quizzes.stats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.quizzes.QuizModel
import com.example.quizappdiploma.database.users.UserModel

@Entity(
    tableName = "quiz_stats",
    foreignKeys = [
        ForeignKey(
            entity = QuizModel::class,
            parentColumns = ["id"],
            childColumns = ["quiz_id"]
        ),
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        )
    ]
)

class QuizStatsModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "user_id")
    val user_id: Int?,
    @ColumnInfo(name = "quiz_id")
    val quiz_id: Int?,
    @ColumnInfo(name = "correct_answers")
    val correctAnswers: Int?
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizStatsModel

        if (id != other.id) return false
        if (user_id != other.user_id) return false
        if (quiz_id != other.quiz_id) return false
        if (correctAnswers != other.correctAnswers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (user_id ?: 0)
        result = 31 * result + (quiz_id ?: 0)
        result = 31 * result + (correctAnswers ?: 0)
        return result
    }

    override fun toString(): String {
        return "QuizStatsModel(id=$id, user_id=$user_id, quiz_id=$quiz_id, correctAnswers=$correctAnswers)"
    }
}