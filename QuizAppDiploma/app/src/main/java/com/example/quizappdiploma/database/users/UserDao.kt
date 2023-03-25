package com.example.quizappdiploma.database.users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizappdiploma.database.courses.CourseModel

@Dao
interface UserDao
{
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserModel?

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): LiveData<List<UserModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserModel): Long
}
