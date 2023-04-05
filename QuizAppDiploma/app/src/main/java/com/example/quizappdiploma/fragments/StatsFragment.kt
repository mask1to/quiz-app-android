package com.example.quizappdiploma.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.lists.StatsAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsDataRepository
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.fragments.viewmodels.QuizStatsViewModel
import com.example.quizappdiploma.fragments.viewmodels.UserViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizStatsViewModelFactory
import com.example.quizappdiploma.fragments.viewmodels.factory.UserViewModelFactory
import kotlinx.coroutines.launch

class StatsFragment : Fragment() {

    private lateinit var statsRecyclerView: RecyclerView
    private lateinit var quizStatsViewModel: QuizStatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                // Save the current navigation state
                val navController = findNavController()
                val navState = navController.saveState()

                // Remove all the previous fragments from the back stack
                // TODO: maybe change fragment
                navController.popBackStack(R.id.statsFragment, true)

                // Minimize the app
                requireActivity().moveTaskToBack(true)

                // Restore the navigation state when the app is resumed
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val dao = MyDatabase.getDatabase(requireContext()).quizStatsDao()
        val repository = QuizStatsDataRepository(dao)
        quizStatsViewModel = ViewModelProvider(this, QuizStatsViewModelFactory(repository))[QuizStatsViewModel::class.java]

        // Fetch stats from the database
        lifecycleScope.launch {
            val quizStats = dao.getAllStats()

            // Update the RecyclerView adapter with the fetched stats
            statsRecyclerView = view.findViewById(R.id.stats_recycler_view)
            statsRecyclerView.layoutManager = LinearLayoutManager(context)
            statsRecyclerView.adapter = StatsAdapter(quizStats)
        }
    }
}