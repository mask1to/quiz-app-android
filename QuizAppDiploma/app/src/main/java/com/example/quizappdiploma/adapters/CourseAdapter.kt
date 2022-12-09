package com.example.quizappdiploma.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.courses.CourseModel
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.navigation.Navigation
import com.example.quizappdiploma.fragments.courses.CourseFragmentDirections

class CourseAdapter(val courseContext : Context) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>()
{
    var courseData : List<CourseModel> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder
    {
        return CourseViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int)
    {
        holder.bindData(courseData[position])
    }

    override fun getItemCount(): Int
    {
        return courseData.size
    }

    class CourseViewHolder(
        private val parent : ViewGroup,
        itemView : View = LayoutInflater.from(parent.context).inflate(
            R.layout.each_course_item, parent, false)
    ) : RecyclerView.ViewHolder(itemView)
    {
        fun bindData(item : CourseModel)
        {
            itemView.findViewById<TextView>(R.id.eachCourseTextView).text = item.courseName
            itemView.findViewById<CardView>(R.id.eachCourseCardView).setOnClickListener {
                val action = CourseFragmentDirections.actionCourseFragmentToLectureFragment()
                Navigation.findNavController(itemView).navigate(action)
            }
        }

    }






}