package com.example.quizappdiploma.fragments.courses

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappdiploma.adapters.CourseAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.databinding.FragmentCourseBinding
import com.example.quizappdiploma.fragments.viewmodels.CourseViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.CourseViewModelFactory

class CourseFragment : Fragment() {

    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var adapter: CourseAdapter
    private var allCourses: List<CourseModel> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CourseAdapter()
        binding.courseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.courseRecyclerView.adapter = adapter

        val dao = MyDatabase.getDatabase(requireContext()).courseDao()
        val repository = CourseDataRepository(dao)
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(repository))[CourseViewModel::class.java]

        courseViewModel.getCoursesByIdAsc().observe(viewLifecycleOwner) { courses ->
            allCourses = courses
            adapter.setData(courses)
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            coursemodel = courseViewModel
        }

        // Search functionality
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim()?.lowercase() ?: ""
                val filtered = if (query.isEmpty()) allCourses
                else allCourses.filter { it.courseName?.lowercase()?.contains(query) == true }
                adapter.setData(filtered)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
