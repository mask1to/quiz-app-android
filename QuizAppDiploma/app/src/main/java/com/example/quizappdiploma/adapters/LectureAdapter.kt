package com.example.quizappdiploma.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder
    {
        return LectureViewHolder(parent)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int)
    {
        holder.bindData(lectureData[position])
    }

    override fun getItemCount(): Int
    {
        return lectureData.size
    }

    class LectureViewHolder(
        private val parent : ViewGroup,
        itemView : View = LayoutInflater.from(parent.context).inflate(
            R.layout.each_lecture_item,
            parent, false)
    ) : RecyclerView.ViewHolder(itemView)
    {
        fun bindData(item : LectureModel)
        {
            itemView.findViewById<TextView>(R.id.lectureNameTxt).text = item.lectureName
            itemView.findViewById<TextView>(R.id.lectureDescTxt).text = item.lectureDescription
            itemView.findViewById<CardView>(R.id.lectureCardView).setOnClickListener {
                // navigate to content
            }

        }
    }








}