package com.example.quizappdiploma.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.quizappdiploma.database.users.UserModel

class PreferenceManager(context: Context)
{

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    fun saveUser(user: UserModel) {
        val editor = sharedPreferences.edit()
        Log.d("Preferences", "Saving user: $user")
        editor.putInt("user_id", user.id ?: -1)
        editor.putString("user_email", user.email)
        editor.putString("username", user.username)
        editor.putString("user_password", user.password)
        editor.putInt("is_admin", user.isAdmin ?: 0)
        editor.putInt("is_lecturer", user.isLecturer ?: 0)
        editor.putInt("is_student", user.isStudent ?: 0)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getInt("user_id", -1) != -1
    }

    fun getLoggedInUser(): UserModel? {
        val id = sharedPreferences.getInt("user_id", -1)
        if (id == -1) {
            return null
        }
        val email = sharedPreferences.getString("user_email", "")
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("user_password", "")
        val isAdmin = sharedPreferences.getInt("is_admin", 0)
        val isLecturer = sharedPreferences.getInt("is_lecturer", 0)
        val isStudent = sharedPreferences.getInt("is_student", 0)
        Log.d("Preferences", "Read values: id=$id, email=$email, username=$username, password=$password, isAdmin=$isAdmin, isLecturer=$isLecturer, isStudent=$isStudent")
        return UserModel(id, email, username, password, isAdmin, isLecturer, isStudent)
    }


    fun logout() {
        val editor = sharedPreferences.edit()
        Log.d("Preferences", "Logout called")
        editor.remove("user_id")
        editor.remove("user_email")
        editor.remove("username")
        editor.remove("user_password")
        editor.remove("is_admin")
        editor.remove("is_lecturer")
        editor.remove("is_student")
        editor.apply()
    }

}

