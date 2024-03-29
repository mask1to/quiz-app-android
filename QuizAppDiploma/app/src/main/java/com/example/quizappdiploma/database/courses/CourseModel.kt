package com.example.quizappdiploma.database.courses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "courses")
class CourseModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    @ColumnInfo(name = "course_name")
    val courseName : String?
)

{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CourseModel

        if (id != other.id) return false
        if (courseName != other.courseName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (courseName?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CourseModel(id=$id, courseName=$courseName)"
    }
}