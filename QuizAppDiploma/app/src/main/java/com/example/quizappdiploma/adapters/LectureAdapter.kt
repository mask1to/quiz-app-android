package com.example.quizappdiploma.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.lectures.LectureModel

class LectureAdapter: RecyclerView.Adapter<LectureAdapter.LectureViewHolder>()
{

    var lectureData : List<LectureModel> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    set(value){
        field = value
        notifyDataSetChanged()
    }

    class LectureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder
    {
        return LectureViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.each_lecture_item, parent, false))
    }

    override fun getItemCount(): Int
    {
        return lectureData.size
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int)
    {
        val currentItem = lectureData[position]
        holder.itemView.findViewById<TextView>(R.id.lectureNameTxt).text = currentItem.lectureName
        holder.itemView.findViewById<TextView>(R.id.lectureDescTxt).text = currentItem.lectureDescription
        holder.itemView.findViewById<CardView>(R.id.lectureCardView).setOnClickListener {
            // navigate to content
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(lecture: List<LectureModel>)
    {
        this.lectureData = lecture
        notifyDataSetChanged()
    }
}