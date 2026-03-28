package com.example.quizappdiploma.adapters.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
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
            expandedIds.clear()
            notifyDataSetChanged()
        }

    var courseNameMap: Map<Int, String> = emptyMap()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val expandedIds = mutableSetOf<Int?>()

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionCard: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.questionCard)
        val questionText: TextView = itemView.findViewById(R.id.questionText)
        val difficultyBadge: TextView = itemView.findViewById(R.id.difficultyBadge)
        val expandHint: TextView = itemView.findViewById(R.id.expandHint)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
        val answersSection: LinearLayout = itemView.findViewById(R.id.answersSection)
        val optionA: TextView = itemView.findViewById(R.id.optionA)
        val optionB: TextView = itemView.findViewById(R.id.optionB)
        val optionC: TextView = itemView.findViewById(R.id.optionC)
        val optionD: TextView = itemView.findViewById(R.id.optionD)
        val correctAnswerLabel: TextView = itemView.findViewById(R.id.correctAnswerLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question_list, parent, false)
        return QuestionViewHolder(view)
    }

    override fun getItemCount() = questionData.size

    @SuppressLint("NotifyDataSetChanged")
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

        // Expandable answers
        val isExpanded = expandedIds.contains(question.id)
        holder.answersSection.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.expandHint.text = if (isExpanded) "Tap to hide answers ▴" else "Tap to see answers ▾"

        if (isExpanded) {
            val correctLetter = when (question.answer) {
                1 -> "A"; 2 -> "B"; 3 -> "C"; 4 -> "D"; else -> "?"
            }
            holder.optionA.text = "A: ${question.questionOptionA ?: ""}"
            holder.optionB.text = "B: ${question.questionOptionB ?: ""}"
            holder.optionC.text = "C: ${question.questionOptionC ?: ""}"
            holder.optionD.text = "D: ${question.questionOptionD ?: ""}"
            holder.correctAnswerLabel.text = "✓ Correct answer: $correctLetter"

            // Highlight correct option
            listOf(holder.optionA, holder.optionB, holder.optionC, holder.optionD)
                .forEachIndexed { index, tv ->
                    tv.setTextColor(
                        if (index + 1 == question.answer)
                            ContextCompat.getColor(holder.itemView.context, R.color.md_primary)
                        else
                            ContextCompat.getColor(holder.itemView.context, R.color.md_on_surface)
                    )
                }
        }

        holder.questionCard.setOnClickListener {
            if (expandedIds.contains(question.id)) {
                expandedIds.remove(question.id)
            } else {
                expandedIds.add(question.id)
            }
            notifyItemChanged(position)
        }

        holder.editBtn.setOnClickListener { onEdit(question) }
        holder.deleteBtn.setOnClickListener { onDelete(question) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(questions: List<QuizQuestionModel>) {
        questionData = questions
    }
}
