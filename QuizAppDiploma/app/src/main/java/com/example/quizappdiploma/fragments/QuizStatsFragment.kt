import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsDataRepository
import com.example.quizappdiploma.fragments.viewmodels.QuizStatsViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizStatsViewModelFactory
import com.example.quizappdiploma.preferences.PreferenceManager
import kotlinx.coroutines.launch

class QuizStatsFragment : Fragment() {

    private lateinit var statsRecyclerView: RecyclerView
    private lateinit var quizStatsViewModel: QuizStatsViewModel
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var titleTextView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val navState = navController.saveState()

                navController.popBackStack(R.id.statsFragment, true)
                requireActivity().moveTaskToBack(true)
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        titleTextView = view.findViewById(R.id.advanced_stats_title)
        statsRecyclerView = view.findViewById(R.id.stats_recycler_view)
        val noStatsTextView = view.findViewById<TextView>(R.id.noDataTxt)
        val dao = MyDatabase.getDatabase(requireContext()).quizStatsDao()
        val repository = QuizStatsDataRepository(dao)
        quizStatsViewModel = ViewModelProvider(this, QuizStatsViewModelFactory(repository))[QuizStatsViewModel::class.java]

        lifecycleScope.launch {
            val loggedInUser = preferenceManager.getLoggedInUser()
            val quizStats = dao.getAllStats(loggedInUser?.id!!)

            if (quizStats.isNotEmpty())
            {
                noStatsTextView.visibility = View.GONE
                statsRecyclerView.layoutManager = LinearLayoutManager(context)
                statsRecyclerView.adapter = StatsAdapter(quizStats)
            }
            else
            {
                noStatsTextView.text = "Please finish any quiz to see stats"
                statsRecyclerView.visibility = View.GONE
                titleTextView.visibility = View.GONE
                noStatsTextView.visibility = View.VISIBLE
            }
        }
    }
}
