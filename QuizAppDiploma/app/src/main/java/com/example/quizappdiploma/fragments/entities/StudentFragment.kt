package com.example.quizappdiploma.fragments.entities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentStudentBinding
import com.example.quizappdiploma.fragments.ProfileFragment
import com.example.quizappdiploma.fragments.QuizStatsFragment
import com.example.quizappdiploma.fragments.UserProfileFragment
import com.example.quizappdiploma.preferences.PreferenceManager

class StudentFragment : Fragment() {

    private var _binding: FragmentStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().moveTaskToBack(true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        checkLoginStatus()

        replaceFragment(ProfileFragment())
        binding.bottomNavigationView.selectedItemId = R.id.home

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(ProfileFragment())
                R.id.courses -> findNavController().navigate(StudentFragmentDirections.actionStudentFragmentToCourseFragment())
                R.id.stats -> replaceFragment(QuizStatsFragment())
                R.id.profile -> replaceFragment(UserProfileFragment())
            }
            true
        }
    }

    fun navigateToStats() {
        binding.bottomNavigationView.selectedItemId = R.id.stats
        replaceFragment(QuizStatsFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    private fun checkLoginStatus() {
        if (preferenceManager.getLoggedInUser() == null) {
            findNavController().navigate(StudentFragmentDirections.actionStudentFragmentToWelcomeFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
