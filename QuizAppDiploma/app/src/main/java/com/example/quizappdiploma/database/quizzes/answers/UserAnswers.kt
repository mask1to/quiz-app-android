package com.example.quizappdiploma.database.quizzes.answers

import androidx.room.*
import com.example.quizappdiploma.database.quizzes.QuizModel
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.database.users.UserModel

@Entity(tableName = "user_answers",
    foreignKeys = [
            ForeignKey(entity = UserModel::class,
                parentColumns = ["id"],
                childColumns = ["user_id"]),
            ForeignKey(entity = QuizQuestionModel::class,
                parentColumns = ["id"],
                childColumns = ["question_id"]),
            ForeignKey(entity = QuizModel::class,
                parentColumns = ["id"],
                childColumns = ["quiz_id"])
        ])
class UserAnswers(
        @PrimaryKey(autoGenerate = true)
        val id: Int?,
        @ColumnInfo(name = "user_id")
        val user_id: Int?,
        @ColumnInfo(name = "question_id")
        val question_id: Int?,
        @ColumnInfo(name = "quiz_id")
        val quiz_id: Int?,
        @ColumnInfo(name = "answer")
        val answer: Int?,
        @ColumnInfo(name = "time_spent")
        val time_spent: Int?)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserAnswers

        if (id != other.id) return false
        if (user_id != other.user_id) return false
        if (question_id != other.question_id) return false
        if (quiz_id != other.quiz_id) return false
        if (answer != other.answer) return false
        if (time_spent != other.time_spent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (user_id ?: 0)
        result = 31 * result + (question_id ?: 0)
        result = 31 * result + (quiz_id ?: 0)
        result = 31 * result + (answer ?: 0)
        result = 31 * result + (time_spent ?: 0)
        return result
    }

    override fun toString(): String {
        return "UserAnswers(id=$id, user_id=$user_id, question_id=$question_id, quiz_id=$quiz_id, answer=$answer, time_spent=$time_spent)"
    }
}