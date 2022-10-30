package com.example.quizappdiploma.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.entities.Lecture
import com.example.quizappdiploma.R
import com.example.quizappdiploma.fragments.courses.BiologyCourseDirections

class LectureAdapter(private val lectureList : ArrayList<Lecture>)
    :RecyclerView.Adapter<LectureAdapter.LectureViewHolder>()
{
    var onItemClick : ((Lecture) -> Unit)? = null

    class LectureViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val lectureCardView : CardView = itemView.findViewById(R.id.eachLectureCardView)
        val imageView : ImageView = itemView.findViewById(R.id.eachImg)
        val textView : TextView = itemView.findViewById(R.id.eachTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_lecture_item, parent, false)
        return LectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectureList[position]
        holder.imageView.setImageResource(lecture.image)
        holder.textView.text = lecture.name

        holder.lectureCardView.setOnClickListener {
            val action = BiologyCourseDirections.actionBiologyCourseToBiologyContent()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return lectureList.size
    }
}