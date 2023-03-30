package com.example.quizappdiploma.adapters.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.CourseAdapter
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.fragments.courses.CourseFragmentDirections

class CourseListAdapter : RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>()
{

    var courseData : List<CourseModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value){
            field = value
            notifyDataSetChanged()
        }
    class CourseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_courselist, parent, false))
    }

    override fun getItemCount(): Int
    {
        return courseData.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int)
    {
        val currentItem = courseData[position]
        holder.itemView.findViewById<TextView>(R.id.courseTitle).text = currentItem.courseName.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(course: List<CourseModel>)
    {
        this.courseData = course
        notifyDataSetChanged()
    }
}