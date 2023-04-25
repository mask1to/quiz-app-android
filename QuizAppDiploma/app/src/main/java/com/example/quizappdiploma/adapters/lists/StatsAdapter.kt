import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsModel

class StatsAdapter(private val statsData: List<QuizStatsModel>) : RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    private val totalQuestions = 10

    class StatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizName: TextView = itemView.findViewById(R.id.quiz_name)
        val points: TextView = itemView.findViewById(R.id.points)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.quiz_stats_item, parent, false)
        return StatsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val currentItem = statsData[position]
        holder.quizName.text = splitString(currentItem.quizName.toString())
        holder.points.text = "Accuracy: ${currentItem.correctAnswers}/$totalQuestions"
    }

    override fun getItemCount() = statsData.size

    private fun splitString(input: String): String {
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
