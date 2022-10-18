package com.example.quizappdiploma.fragments.courses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.example.quizappdiploma.R


class BiologyCourse : Fragment()
{

    private lateinit var firstLecture : Button
    private lateinit var secondLecture : Button

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
        firstLecture = view.findViewById(R.id.learnBioFirst)
        secondLecture = view.findViewById(R.id.learnBioSecond)

        firstLecture.setOnClickListener{
            val action = BiologyCourseDirections.actionBiologyCourseToFirstBioLecture()
            view.findNavController().navigate(action)
        }

        secondLecture.setOnClickListener {
            val action = BiologyCourseDirections.actionBiologyCourseToSecondBioLecture()
            view.findNavController().navigate(action)
        }

        super.onViewCreated(view, savedInstanceState)
    }

}