package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.databinding.FragmentProfileBinding
import com.example.quizappdiploma.fragments.entities.StudentFragmentDirections
import com.example.quizappdiploma.preferences.PreferenceManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())

        val user = preferenceManager.getLoggedInUser()
        if (user != null) {
            binding.userNameTxt.text = user.username ?: "Student"
        }

        binding.courseBtn.setOnClickListener {
            findNavController().navigate(StudentFragmentDirections.actionStudentFragmentToCourseFragment())
        }

        binding.statsQuickCard.setOnClickListener {
            (parentFragment as? com.example.quizappdiploma.fragments.entities.StudentFragment)
                ?.navigateToStats()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
