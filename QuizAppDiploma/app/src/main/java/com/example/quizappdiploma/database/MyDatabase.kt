package com.example.quizappdiploma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizappdiploma.database.courses.CourseDao
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureDao
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.database.quizzes.QuizDao
import com.example.quizappdiploma.database.quizzes.QuizModel
import com.example.quizappdiploma.database.quizzes.answers.UserAnswers
import com.example.quizappdiploma.database.quizzes.answers.UserAnswersDao
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDao
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsDao
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsModel
import com.example.quizappdiploma.database.users.UserDao
import com.example.quizappdiploma.database.users.UserModel

@Database(
    entities = [UserModel::class, CourseModel::class,
                LectureModel::class, QuizModel::class,
                QuizQuestionModel::class, QuizStatsModel::class,
                UserAnswers::class],
    version = 2)
abstract class MyDatabase : RoomDatabase()
{

    abstract fun myDatabaseDao(): MyDatabaseDao
    abstract fun courseDao(): CourseDao
    abstract fun lectureDao(): LectureDao
    abstract fun quizDao() : QuizDao
    abstract fun quizQuestionDao(): QuizQuestionDao
    abstract fun userDao(): UserDao
    abstract fun quizStatsDao() : QuizStatsDao
    abstract fun userAnswersDao() : UserAnswersDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null
        private const val  DATABASE_NAME = "quiz_db"

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null) {
                    synchronized(this) {
                        INSTANCE = Room.databaseBuilder(context, MyDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .createFromAsset("database/quiz_db.db")
                            .build()
                    }
                }
                INSTANCE!!
            }
        }

    }

}