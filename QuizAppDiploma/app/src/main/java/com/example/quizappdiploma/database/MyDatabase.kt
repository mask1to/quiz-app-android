package com.example.quizappdiploma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
    version = 1,
    exportSchema = false)
abstract class MyDatabase : RoomDatabase()
{
    abstract fun myDatabaseDao(): MyDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "quiz_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}