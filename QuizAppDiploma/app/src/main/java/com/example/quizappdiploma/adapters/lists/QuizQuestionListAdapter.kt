package com.example.quizappdiploma.adapters.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel

class QuizQuestionListAdapter(
    private val onEdit: (QuizQuestionModel) -> Unit,
    private val onDelete: (QuizQuestionModel) -> Unit
) : RecyclerView.Adapter<QuizQuestionListAdapter.QuestionViewHolder>() {

    var questionData: List<QuizQuestionModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var courseNameMap: Map<Int, String> = emptyMap()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionText: TextView = itemView.findViewById(R.id.questionText)
        val difficultyBadge: TextView = itemView.findViewById(R.id.difficultyBadge)
        val courseLabel: TextView = itemView.findViewById(R.id.courseLabel)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question_list, parent, false)
        return QuestionViewHolder(view)
    }

    override fun getItemCount() = questionData.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionData[position]
        holder.questionText.text = question.questionName ?: "No question text"

        val (diffText, bgRes) = when (question.questionDifficulty) {
            1 -> Pair("Easy", R.drawable.bg_chip_difficulty_easy)
            2 -> Pair("Medium", R.drawable.bg_chip_difficulty_medium)
            3 -> Pair("Hard", R.drawable.bg_chip_difficulty_hard)
            else -> Pair("Unknown", R.drawable.bg_chip_difficulty_easy)
        }
        holder.difficultyBadge.text = diffText
        holder.difficultyBadge.background = ContextCompat.getDrawable(holder.itemView.context, bgRes)

        val courseName = question.courseId?.let { courseNameMap[it] } ?: "Course #${question.courseId}"
        holder.courseLabel.text = courseName

        holder.editBtn.setOnClickListener { onEdit(question) }
        holder.deleteBtn.setOnClickListener { onDelete(question) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(questions: List<QuizQuestionModel>) {
        questionData = questions
    }
}
