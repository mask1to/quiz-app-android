package com.example.quizappdiploma.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.fragments.courses.CourseFragmentDirections

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private val courseColors = intArrayOf(
        R.color.course_color_1,
        R.color.course_color_2,
        R.color.course_color_3,
        R.color.course_color_4,
        R.color.course_color_5,
        R.color.course_color_6
    )

    var courseData: List<CourseModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.each_course_item, parent, false)
        )
    }

    override fun getItemCount() = courseData.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentItem = courseData[position]
        val ctx = holder.itemView.context

        holder.itemView.findViewById<TextView>(R.id.eachCourseTextView).text = currentItem.courseName ?: ""

        // Apply per-course gradient color
        val colorRes = courseColors[position % courseColors.size]
        val color = ContextCompat.getColor(ctx, colorRes)
        val darkerColor = darkenColor(color, 0.7f)
        val bg = holder.itemView.findViewById<LinearLayout>(R.id.courseCardBg)
        val gradient = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(color, darkerColor))
        gradient.cornerRadius = ctx.resources.getDimension(R.dimen.card_corner_radius)
        bg.background = gradient

        holder.itemView.findViewById<CardView>(R.id.eachCourseCardView).setOnClickListener {
            val action = CourseFragmentDirections.actionCourseFragmentToLectureFragment(currentItem.id!!)
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(course: List<CourseModel>) {
        this.courseData = course
        notifyDataSetChanged()
    }

    private fun darkenColor(color: Int, factor: Float): Int {
        val a = android.graphics.Color.alpha(color)
        val r = (android.graphics.Color.red(color) * factor).toInt()
        val g = (android.graphics.Color.green(color) * factor).toInt()
        val b = (android.graphics.Color.blue(color) * factor).toInt()
        return android.graphics.Color.argb(a, r, g, b)
    }
}
