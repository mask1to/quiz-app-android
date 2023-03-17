package com.example.quizappdiploma.fragments.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappdiploma.adapters.lists.CourseListAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.databinding.FragmentCourseListBinding
import com.example.quizappdiploma.fragments.viewmodels.CourseViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.CourseViewModelFactory
import kotlinx.android.synthetic.main.fragment_course_list.*

class CourseListFragment : Fragment()
{

    private var _binding : FragmentCourseListBinding? = null
    private val binding get() = _binding!!
    private lateinit var courseViewModel : CourseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val dao = MyDatabase.getDatabase(requireContext()).courseDao()
        val repository = CourseDataRepository(dao)
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(repository))[CourseViewModel::class.java]


        val adapter = CourseListAdapter()
        courseList.layoutManager = LinearLayoutManager(requireContext())
        courseList.adapter = adapter

        courseViewModel.getCoursesByIdAsc().observe(viewLifecycleOwner) { courses ->
            adapter.setData(courses)
        }
    }
}