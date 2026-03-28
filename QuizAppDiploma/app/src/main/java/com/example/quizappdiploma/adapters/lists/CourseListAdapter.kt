package com.example.quizappdiploma.adapters.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.courses.CourseModel

class CourseListAdapter(
    private val onEdit: (CourseModel) -> Unit,
    private val onDelete: (CourseModel) -> Unit
) : RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>() {

    var courseData: List<CourseModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.courseTitle)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_courselist, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount() = courseData.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseData[position]
        holder.title.text = course.courseName
        holder.editBtn.setOnClickListener { onEdit(course) }
        holder.deleteBtn.setOnClickListener { onDelete(course) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(courses: List<CourseModel>) {
        courseData = courses
    }
}
