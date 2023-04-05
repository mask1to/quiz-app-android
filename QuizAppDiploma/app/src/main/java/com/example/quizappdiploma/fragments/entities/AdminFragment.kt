package com.example.quizappdiploma.fragments.entities

import ProfileFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentAdminBinding
import com.example.quizappdiploma.fragments.lists.CourseListFragment
import com.example.quizappdiploma.fragments.lists.UserListFragment
import com.example.quizappdiploma.preferences.PreferenceManager

class AdminFragment : Fragment()
{

    private var _binding : FragmentAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    private val usersList = UserListFragment()
    private val coursesList = CourseListFragment()
    private val lecturesList = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        //checkLoginStatus()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
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
                //TODO: change the fragment
                navController.popBackStack(R.id.courseFragment, true)

                // Minimize the app
                requireActivity().moveTaskToBack(true)

                // Restore the navigation state when the app is resumed
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // Set up initial fragment to be displayed
        switchFragment(usersList)

        // Set up navigation buttons
        binding.usersButton.setOnClickListener { switchFragment(usersList) }
        binding.coursesButton.setOnClickListener { switchFragment(coursesList) }
        binding.lecturesButton.setOnClickListener { switchFragment(lecturesList) }
    }

    private fun switchFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }


}