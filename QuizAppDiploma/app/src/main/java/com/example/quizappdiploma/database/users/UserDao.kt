package com.example.quizappdiploma.database.users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.quizappdiploma.database.courses.CourseModel

@Dao
interface UserDao
{
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): LiveData<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): LiveData<List<UserModel>>
}
