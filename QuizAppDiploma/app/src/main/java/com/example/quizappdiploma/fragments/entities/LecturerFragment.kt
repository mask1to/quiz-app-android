package com.example.quizappdiploma.fragments.entities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentLecturerBinding
import com.example.quizappdiploma.fragments.lists.CourseListFragment
import com.example.quizappdiploma.fragments.lists.LectureListFragment
import com.example.quizappdiploma.fragments.lists.QuizQuestionListFragment
import com.example.quizappdiploma.preferences.PreferenceManager

class LecturerFragment : Fragment() {

    private var _binding: FragmentLecturerBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    private val coursesFragment = CourseListFragment()
    private val lecturesFragment = LectureListFragment()
    private val questionsFragment = QuizQuestionListFragment()

    private val tabButtons by lazy {
        listOf(binding.tabCourses, binding.tabLectures, binding.tabQuestions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLecturerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { requireActivity().moveTaskToBack(true) }
        })

        val user = preferenceManager.getLoggedInUser()
        binding.nameTextView.text = user?.username ?: "Lecturer"
        binding.emailTextView.text = user?.email ?: ""

        binding.tabCourses.setOnClickListener { selectTab(0) }
        binding.tabLectures.setOnClickListener { selectTab(1) }
        binding.tabQuestions.setOnClickListener { selectTab(2) }

        binding.logoutButton.setOnClickListener {
            preferenceManager.logout()
            findNavController().navigate(R.id.action_lecturerFragment_to_welcomeFragment)
        }

        binding.importButton.setOnClickListener {
            findNavController().navigate(R.id.action_lecturerFragment_to_importContentFragment)
        }

        selectTab(0)
    }

    private fun selectTab(index: Int) {
        val fragments = listOf(coursesFragment, lecturesFragment, questionsFragment)
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragments[index])
            .commit()

        tabButtons.forEachIndexed { i, btn ->
            btn.isSelected = i == index
            btn.alpha = if (i == index) 1f else 0.6f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
