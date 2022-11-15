package com.example.quizappdiploma.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.entities.Course
import com.example.quizappdiploma.entities.Lecture
import com.example.quizappdiploma.fragments.StudentFragmentDirections
import com.example.quizappdiploma.interfaces.RecyclerInterface

class CourseAdapter(private val courseList : ArrayList<Course>)
    : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>()
{
    class CourseViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val courseCardView : CardView = itemView.findViewById(R.id.eachCourseCardView)
        val imageView : ImageView = itemView.findViewById(R.id.eachCourseImg)
        val textView : TextView = itemView.findViewById(R.id.eachCourseTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_course_item, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int)
    {
        val course = courseList[position]
        holder.imageView.setImageResource(course.image)
        holder.textView.text = course.name

        holder.courseCardView.setOnClickListener {

            val action = StudentFragmentDirections.actionStudentFragmentToBiologyCourse()
            holder.itemView.findNavController().navigate(action)
            Log.d("CourseAdapter", "Click item")
        }
    }

    override fun getItemCount(): Int
    {
        return courseList.size
    }
}