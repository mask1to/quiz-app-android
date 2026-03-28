package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.databinding.FragmentUserProfileBinding
import com.example.quizappdiploma.fragments.entities.StudentFragmentDirections
import com.example.quizappdiploma.preferences.PreferenceManager

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())

        val user = preferenceManager.getLoggedInUser()
        if (user != null) {
            val initials = user.username?.firstOrNull()?.uppercaseChar()?.toString() ?: "S"
            binding.avatarText.text = initials
            binding.profileName.text = user.username ?: "—"
            binding.profileEmail.text = user.email ?: "—"
            binding.profileNameValue.text = user.username ?: "—"
            binding.profileEmailValue.text = user.email ?: "—"
            binding.profileRole.text = when {
                user.isStudent == 1 -> "Student"
                user.isLecturer == 1 -> "Lecturer"
                user.isAdmin == 1 -> "Admin"
                else -> "Student"
            }
        }

        binding.logoutBtn.setOnClickListener {
            preferenceManager.logout()
            findNavController().navigate(StudentFragmentDirections.actionStudentFragmentToWelcomeFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
