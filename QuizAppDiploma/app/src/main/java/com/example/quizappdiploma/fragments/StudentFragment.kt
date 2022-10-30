package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.CourseAdapter
import com.example.quizappdiploma.entities.Course

class StudentFragment : Fragment()
{

    private lateinit var recyclerView : RecyclerView
    private lateinit var courseList : ArrayList<Course>
    private lateinit var courseAdapter : CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //disabled onBackPressed
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_student_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        recyclerView = view.findViewById(R.id.recyclerViewStudentFragment)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        courseList = ArrayList()
        courseList.add(Course(R.drawable.ic_baseline_email_24, "Prvý kurz"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "Druhý kurz"))

        courseAdapter = CourseAdapter(courseList)
        recyclerView.adapter = courseAdapter

        super.onViewCreated(view, savedInstanceState)
    }

}


