package com.example.quizappdiploma.database.lectures

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.quizappdiploma.database.courses.CourseModel
import org.jetbrains.annotations.NotNull

@Entity(tableName = "lectures",
        foreignKeys = [ForeignKey(
            entity = CourseModel::class,
            childColumns = ["course_id"],
            parentColumns = ["id"]
        )])
class LectureModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "course_id")
    val course_id : Int,
    @ColumnInfo(name = "lecture_name")
    val lectureName : String,
    @ColumnInfo(name = "lecture_description")
    val lectureDescription : String,
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LectureModel

        if (id != other.id) return false
        if (course_id != other.course_id) return false
        if (lectureName != other.lectureName) return false
        if (lectureDescription != other.lectureDescription) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + course_id
        result = 31 * result + lectureName.hashCode()
        result = 31 * result + lectureDescription.hashCode()
        return result
    }

    override fun toString(): String {
        return "LectureModel(id=$id, course_id=$course_id, lectureName='$lectureName', lectureDescription='$lectureDescription')"
    }


}