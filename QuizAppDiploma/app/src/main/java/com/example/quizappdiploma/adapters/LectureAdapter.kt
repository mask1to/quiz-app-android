package com.example.quizappdiploma.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.fragments.lectures.LectureFragmentDirections
import kotlinx.coroutines.selects.select

class LectureAdapter(private val courseId : Int): RecyclerView.Adapter<LectureAdapter.LectureViewHolder>()
{

    var lectureData : List<LectureModel> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    set(value){
        field = value
        notifyDataSetChanged()
    }

    class LectureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder
    {
        return LectureViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.each_lecture_item, parent, false))
    }

    override fun getItemCount(): Int
    {
        return lectureData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LectureViewHolder, position: Int)
    {
        val currentItem = lectureData[position]

        holder.itemView.findViewById<TextView>(R.id.lectureNameTxt).text = splitString(currentItem.lectureName.toString())
        //holder.itemView.findViewById<TextView>(R.id.lectureDescTxt).text = currentItem.lectureDescription
        holder.itemView.findViewById<CardView>(R.id.lectureCardView).setOnClickListener {
            val action = LectureFragmentDirections.actionLectureFragmentToContentFragment(
                currentItem.lectureName.toString(),
                currentItem.lectureDescription.toString(),
                currentItem.image_path.toString(),
                courseId,
                currentItem.id!!
            )
            Log.d("lecture id:", currentItem.id.toString())
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(lecture: List<LectureModel>)
    {
        this.lectureData = lecture
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