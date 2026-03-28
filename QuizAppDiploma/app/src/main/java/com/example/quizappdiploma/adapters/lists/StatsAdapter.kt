package com.example.quizappdiploma.adapters.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsModel

class StatsAdapter(private val statsData: List<QuizStatsModel>) :
    RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    private val totalQuestions = 10

    class StatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizName: TextView = itemView.findViewById(R.id.quiz_name)
        val points: TextView = itemView.findViewById(R.id.points)
        val scoreBadge: TextView = itemView.findViewById(R.id.scoreBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.quiz_stats_item, parent, false)
        return StatsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val ctx = holder.itemView.context
        val item = statsData[position]
        val correct = item.correctAnswers ?: 0
        val percent = (correct * 100) / totalQuestions

        holder.quizName.text = item.quizName ?: "Quiz"
        holder.points.text = "Score: $correct/$totalQuestions"
        holder.scoreBadge.text = "$percent%"

        when {
            percent >= 70 -> {
                holder.scoreBadge.setTextColor(ContextCompat.getColor(ctx, R.color.answer_correct_border))
                holder.scoreBadge.background = ContextCompat.getDrawable(ctx, R.drawable.bg_answer_correct)
            }
            percent >= 40 -> {
                holder.scoreBadge.setTextColor(ContextCompat.getColor(ctx, R.color.difficulty_medium))
                holder.scoreBadge.background = ContextCompat.getDrawable(ctx, R.drawable.bg_answer_selected)
            }
            else -> {
                holder.scoreBadge.setTextColor(ContextCompat.getColor(ctx, R.color.answer_wrong_border))
                holder.scoreBadge.background = ContextCompat.getDrawable(ctx, R.drawable.bg_answer_wrong)
            }
        }
    }

    override fun getItemCount() = statsData.size
}
