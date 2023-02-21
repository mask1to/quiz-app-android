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
        holder.itemView.findViewById<TextView>(R.id.eachCourseTextView).text = currentItem.courseName
        holder.itemView.findViewById<CardView>(R.id.eachCourseCardView).setOnClickListener {
            //lectureViewModel.getLecturesByCourseName(currentItem.courseName)
            //TODO: check if id is being passed correctly
            val action = CourseFragmentDirections.actionCourseFragmentToLectureFragment(currentItem.id!!)
            Log.d("id", currentItem.id.toString())
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(course: List<CourseModel>)
    {
        this.courseData = course
        notifyDataSetChanged()
    }

}