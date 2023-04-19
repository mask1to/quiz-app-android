package com.example.quizappdiploma.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Perform any necessary migration steps here
        // If no changes are required, leave this empty
    }
}

