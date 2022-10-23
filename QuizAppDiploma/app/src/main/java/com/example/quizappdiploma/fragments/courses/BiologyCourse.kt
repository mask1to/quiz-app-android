package com.example.quizappdiploma.fragments.courses

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.Course
import com.example.quizappdiploma.CourseAdapter
import com.example.quizappdiploma.R
import com.example.quizappdiploma.fragments.courses.lectures.BioLecture


class BiologyCourse : Fragment()
{

    private lateinit var recyclerView: RecyclerView
    private lateinit var courseList : ArrayList<Course>
    private lateinit var courseAdapter: CourseAdapter

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

        return inflater.inflate(R.layout.fragment_biology_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        courseList = ArrayList()
        courseList.add(Course(R.drawable.ic_baseline_email_24, "Ahoj"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "Ako"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "sa"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "mas"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "Ahoj"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "ako"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "sa"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "mas"))
        courseList.add(Course(R.drawable.ic_baseline_email_24, "Ahoj"))


        courseAdapter = CourseAdapter(courseList)
        recyclerView.adapter = courseAdapter

        courseAdapter.onItemClick = {
            val intent = Intent(context, BioLecture::class.java)
            intent.putExtra("course", it)
            startActivity(intent)
        }

        super.onViewCreated(view, savedInstanceState)
    }

}