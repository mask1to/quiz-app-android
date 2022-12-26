package com.example.quizappdiploma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizappdiploma.database.courses.CourseDao
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.database.quizzes.QuizModel
import com.example.quizappdiploma.database.quizzes.answers.QuizAnswerModel
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.database.users.UserModel

@Database(
    entities = [UserModel::class, CourseModel::class,
                LectureModel::class, QuizModel::class,
                QuizQuestionModel::class, QuizAnswerModel::class],
    version = 1)
abstract class MyDatabase : RoomDatabase()
{

    abstract fun myDatabaseDao(): MyDatabaseDao
    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null
        private const val  DATABASE_NAME = "quiz_db"

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this)
            {
                if (INSTANCE == null) {
                    synchronized(this) {
                        INSTANCE =
                            Room.databaseBuilder(context, MyDatabase::class.java, DATABASE_NAME)
                                .fallbackToDestructiveMigration()
                                .createFromAsset("database/quiz_db.db")
                                .build()
                    }
                }
                return INSTANCE!!
            }
        }
    }

}