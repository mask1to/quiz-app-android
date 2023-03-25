package com.example.quizappdiploma.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.quizappdiploma.database.users.UserModel

class PreferenceManager(context: Context) {
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "SharedPreferences"

    private val IS_LOGIN = "is_login"
    private val USER_ID = "user_id"
    private val USER_EMAIL = "user_email"
    private val USER_NICKNAME = "user_nickname"
    private val USER_PASSWORD = "user_password"
    private val IS_ADMIN = "is_admin"
    private val IS_LECTURER = "is_lecturer"
    private val IS_STUDENT = "is_student"

    private val preferences: SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor? = preferences?.edit()

    fun setLogin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.apply()
    }
    fun isLogin(): Boolean? {
        return preferences?.getBoolean(IS_LOGIN, false)
    }

    fun setLoggedInUser(user: UserModel) {
        Log.d("PreferenceManager", "Storing user: $user")
        editor?.putInt(USER_ID, user.id!!)
        editor?.putString(USER_EMAIL, user.email)
        editor?.putString(USER_NICKNAME, user.username)
        editor?.putString(USER_PASSWORD, user.password)
        editor?.putInt(IS_ADMIN, user.isAdmin!!)
        editor?.putInt(IS_LECTURER, user.isLecturer!!)
        editor?.putInt(IS_STUDENT, user.isStudent!!)
        editor?.apply()
    }

    fun getLoggedInUser(): UserModel? {
        val id = preferences?.getInt(USER_ID, -1) ?: -1
        if (id == -1) {
            return null
        }
        val email = preferences?.getString(USER_EMAIL, "") ?: ""
        val nickname = preferences?.getString(USER_NICKNAME, "") ?: ""
        val password = preferences?.getString(USER_PASSWORD, "") ?: ""
        val isAdmin = preferences?.getInt(IS_ADMIN, 0)
        val isLecturer = preferences?.getInt(IS_LECTURER, 0)
        val isStudent = preferences?.getInt(IS_STUDENT, 0)

        val retrievedUser = UserModel(id, email, nickname, password, isAdmin, isLecturer, isStudent)
        Log.d("PreferenceManager", "Retrieved user: $retrievedUser")
        return retrievedUser
    }

    fun clearLoggedInUser() {
        editor?.remove(USER_ID)
        editor?.remove(USER_EMAIL)
        editor?.remove(USER_NICKNAME)
        editor?.remove(USER_PASSWORD)
        editor?.remove(IS_ADMIN)
        editor?.remove(IS_LECTURER)
        editor?.remove(IS_STUDENT)
        editor?.apply()
    }
    fun removeData() {
        editor?.clear()
        editor?.apply()
    }
}
