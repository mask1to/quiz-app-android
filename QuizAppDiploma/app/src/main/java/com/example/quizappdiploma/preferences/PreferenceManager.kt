package com.example.quizappdiploma.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context)
{
    val PRIVATE_MODE = 0

    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    val preferences : SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    val editor : SharedPreferences.Editor? = preferences?.edit()

    fun setLogin(isLogin : Boolean)
    {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun setUsername(username : String?)
    {
        editor?.putString("username", username)
        editor?.commit()
    }

    fun isLogin() : Boolean?
    {
        return preferences?.getBoolean(IS_LOGIN, false)
    }

    fun getUsername() : String?
    {
        return preferences?.getString("username", "")
    }

    fun removeData()
    {
        editor?.clear()
        editor?.commit()
    }
}