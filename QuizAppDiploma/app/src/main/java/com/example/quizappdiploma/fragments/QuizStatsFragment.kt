package com.example.quizappdiploma.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.lists.StatsAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsDataRepository
import com.example.quizappdiploma.databinding.FragmentQuizStatsBinding
import com.example.quizappdiploma.fragments.viewmodels.QuizStatsViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizStatsViewModelFactory
import com.example.quizappdiploma.preferences.PreferenceManager
import kotlinx.coroutines.launch

class QuizStatsFragment : Fragment() {

    private var _binding: FragmentQuizStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var quizStatsViewModel: QuizStatsViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
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

        val dao = MyDatabase.getDatabase(requireContext()).quizStatsDao()
        val repository = QuizStatsDataRepository(dao)
        quizStatsViewModel = ViewModelProvider(this, QuizStatsViewModelFactory(repository))[QuizStatsViewModel::class.java]

        lifecycleScope.launch {
            val loggedInUser = preferenceManager.getLoggedInUser()
            val quizStats = dao.getAllStats(loggedInUser?.id!!)

            if (quizStats.isNotEmpty()) {
                val totalQuestions = 10
                val totalQuizzes = quizStats.size
                val bestScore = quizStats.maxOf { it.correctAnswers ?: 0 }
                val avgAccuracy = quizStats.map { (it.correctAnswers ?: 0) * 100 / totalQuestions }.average().toInt()

                binding.statTotalQuizzes.text = totalQuizzes.toString()
                binding.statBestScore.text = "$bestScore/$totalQuestions"
                binding.statAvgAccuracy.text = "$avgAccuracy%"

                binding.summaryRow.visibility = View.VISIBLE
                binding.advancedStatsTitle.visibility = View.VISIBLE
                binding.statsRecyclerView.visibility = View.VISIBLE
                binding.emptyState.visibility = View.GONE

                binding.statsRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.statsRecyclerView.adapter = StatsAdapter(quizStats)
            } else {
                binding.emptyState.visibility = View.VISIBLE
                binding.summaryRow.visibility = View.GONE
                binding.advancedStatsTitle.visibility = View.GONE
                binding.statsRecyclerView.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
