package com.example.quizappdiploma.database.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "users")
class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    @ColumnInfo(name = "email")
    val email : String?,
    @ColumnInfo(name = "username")
    val username : String?,
    @ColumnInfo(name = "password")
    val password : String?,
    @ColumnInfo(name = "is_admin")
    val isAdmin : Int?,
    @ColumnInfo(name = "is_lecturer")
    val isLecturer : Int?,
    @ColumnInfo(name = "is_student")
    val isStudent : Int?
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (id != other.id) return false
        if (email != other.email) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (isAdmin != other.isAdmin) return false
        if (isLecturer != other.isLecturer) return false
        if (isStudent != other.isStudent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (isAdmin ?: 0)
        result = 31 * result + (isLecturer ?: 0)
        result = 31 * result + (isStudent ?: 0)
        return result
    }

    override fun toString(): String {
        return "UserModel(id=$id, email=$email, username=$username, password=$password, isAdmin=$isAdmin, isLecturer=$isLecturer, isStudent=$isStudent)"
    }

}