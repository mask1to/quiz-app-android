package com.example.quizappdiploma.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object
    {
        /*private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "xxx"
        private val TABLE_CONTACTS = "yyy"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"*/
    }

    override fun onCreate(p0: SQLiteDatabase?)
    {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int)
    {
        TODO("Not yet implemented")
    }

}
