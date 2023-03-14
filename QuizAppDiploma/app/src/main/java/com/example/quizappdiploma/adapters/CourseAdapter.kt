package com.example.quizappdiploma.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.courses.CourseModel
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.quizappdiploma.fragments.courses.CourseFragmentDirections
import com.example.quizappdiploma.fragments.viewmodels.LectureViewModel

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>()
{
    var courseData : List<CourseModel> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    set(value){
        field = value
        notifyDataSetChanged()
    }

    class CourseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.each_course_item, parent, false))
    }

    override fun getItemCount(): Int
    {
        return courseData.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int)
    {
        val currentItem = courseData[position]

        holder.itemView.findViewById<TextView>(R.id.eachCourseTextView).text = splitString(currentItem.courseName.toString())
        holder.itemView.findViewById<CardView>(R.id.eachCourseCardView).setOnClickListener {
            val action = CourseFragmentDirections.actionCourseFragmentToLectureFragment(currentItem.id!!)
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(course: List<CourseModel>)
    {
        this.courseData = course
        notifyDataSetChanged()
    }

    fun splitString(input: String): String {
        val maxCharsPerLine = 15
        val words = input.split(" ")
        var lineLength = 0
        var result = ""
        for (word in words) {
            if (lineLength + word.length > maxCharsPerLine) {
                result += "\n"
                lineLength = 0
            }
            result += "$word "
            lineLength += word.length + 1
        }
        return result.trim()
    }


}