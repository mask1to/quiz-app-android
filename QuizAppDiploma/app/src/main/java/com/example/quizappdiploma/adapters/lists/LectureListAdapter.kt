package com.example.quizappdiploma.adapters.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.lectures.LectureModel

class LectureListAdapter(
    private val onEdit: (LectureModel) -> Unit,
    private val onDelete: (LectureModel) -> Unit
) : RecyclerView.Adapter<LectureListAdapter.LectureViewHolder>() {

    var lectureData: List<LectureModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var courseNameMap: Map<Int, String> = emptyMap()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class LectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.lectureTitle)
        val courseLabel: TextView = itemView.findViewById(R.id.lectureCourseLabel)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lecture_list, parent, false)
        return LectureViewHolder(view)
    }

    override fun getItemCount() = lectureData.size

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectureData[position]
        holder.title.text = lecture.lectureName ?: "Untitled lecture"
        val courseName = lecture.course_id?.let { courseNameMap[it] } ?: "Course #${lecture.course_id}"
        holder.courseLabel.text = courseName
        holder.editBtn.setOnClickListener { onEdit(lecture) }
        holder.deleteBtn.setOnClickListener { onDelete(lecture) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(lectures: List<LectureModel>) {
        lectureData = lectures
    }
}
