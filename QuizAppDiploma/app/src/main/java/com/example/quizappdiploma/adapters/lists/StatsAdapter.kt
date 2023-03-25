package com.example.quizappdiploma.adapters.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsModel

class StatsAdapter(private val statsData: List<QuizStatsModel>) : RecyclerView.Adapter<StatsAdapter.ViewHolder>(){

    private val totalQuestions = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quizStat = statsData[position]
        holder.statTextView.text = "${quizStat.quiz_id}: ${quizStat.correctAnswers}/${totalQuestions}"
    }

    override fun getItemCount() = statsData.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statTextView: TextView = view.findViewById(R.id.stat_text_view)
    }
}