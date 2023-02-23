package com.example.quizappdiploma.fragments.lectures

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.adapters.LectureAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.databinding.FragmentLectureBinding
import com.example.quizappdiploma.fragments.viewmodels.LectureViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.LectureViewModelFactory

class LectureFragment : Fragment()
{
    private var _binding : FragmentLectureBinding? = null
    private val binding get() = _binding!!
    private val args : LectureFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var lectureViewModel: LectureViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentLectureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LectureAdapter()
        recyclerView = binding.lectureRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val dao = MyDatabase.getDatabase(requireContext()).lectureDao()
        val repository = LectureDataRepository(dao)
        lectureViewModel = ViewModelProvider(this, LectureViewModelFactory(repository)).get(LectureViewModel::class.java)

        lectureViewModel.getLecturesByCourseId(args.courseId).observe(viewLifecycleOwner){lectures ->
            adapter.setData(lectures)
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            lecturemodel = lectureViewModel
        }

        Log.d("id in fragment:", args.courseId.toString())

    }
}