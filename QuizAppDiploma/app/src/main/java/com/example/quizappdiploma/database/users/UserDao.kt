package com.example.quizappdiploma.database.users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao
{
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserModel?
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): LiveData<List<UserModel>>

    @Query("SELECT id, username, email FROM users WHERE username= :username AND email= :email")
    fun getUserByUsernameAndEmail(username : String, email: String): LiveData<List<UserModel>>

    @Query("SELECT id, email FROM users WHERE email= :email")
    fun getUserByEmail(email : String) : LiveData<List<UserModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserModel)
    @Update
    suspend fun updateUser(user: UserModel)
    @Delete
    suspend fun deleteUser(user : UserModel)
}
