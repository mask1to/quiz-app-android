package com.example.quizappdiploma.fragments.courses

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.adapters.CourseAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.databinding.FragmentCourseBinding
import com.example.quizappdiploma.fragments.viewmodels.CourseViewModel
import com.example.quizappdiploma.fragments.viewmodels.LectureViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.CourseViewModelFactory


class CourseFragment : Fragment()
{
    private var _binding : FragmentCourseBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseViewModel : CourseViewModel
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {

        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CourseAdapter()
        recyclerView = binding.courseRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val dao = MyDatabase.getDatabase(requireContext()).courseDao()
        val repository = CourseDataRepository(dao)
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(repository))[CourseViewModel::class.java]

        courseViewModel.getCoursesByIdAsc().observe(viewLifecycleOwner) { courses ->
            adapter.setData(courses)
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            coursemodel = courseViewModel
        }

    }

}