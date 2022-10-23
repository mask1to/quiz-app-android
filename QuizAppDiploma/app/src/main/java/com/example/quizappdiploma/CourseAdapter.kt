package com.example.quizappdiploma

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(private val courseList : ArrayList<Course>)
    :RecyclerView.Adapter<CourseAdapter.CourseViewHolder>()
{
    var onItemClick : ((Course) -> Unit)? = null

    class CourseViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val imageView : ImageView = itemView.findViewById(R.id.eachImg)
        val textView : TextView = itemView.findViewById(R.id.eachTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.imageView.setImageResource(course.image)
        holder.textView.text = course.name

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(course)
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }
}