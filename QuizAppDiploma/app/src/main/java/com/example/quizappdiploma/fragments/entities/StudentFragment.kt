package com.example.quizappdiploma.fragments.entities

import ProfileFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentStudentBinding
import com.example.quizappdiploma.fragments.StatsFragment
import com.example.quizappdiploma.preferences.PreferenceManager

class StudentFragment : Fragment() {

    private var _binding: FragmentStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentBinding.inflate(inflater, container, false)
        return binding.root
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
                navController.popBackStack(R.id.courseFragment, true)

                // Minimize the app
                requireActivity().moveTaskToBack(true)

                // Restore the navigation state when the app is resumed
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        checkLoginStatus()

        replaceFragment(ProfileFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> logout()
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.stats -> replaceFragment(StatsFragment())
                else -> { }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun checkLoginStatus() {
        val loggedInUser = preferenceManager.getLoggedInUser()
        if (loggedInUser == null) {
            val action = StudentFragmentDirections.actionStudentFragmentToWelcomeFragment()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Login status true", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logout() {
        preferenceManager.logout()
        val action = StudentFragmentDirections.actionStudentFragmentToWelcomeFragment()
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
