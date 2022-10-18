package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.quizappdiploma.R

class StudentFragment : Fragment()
{

    private lateinit var showFirstBtn : Button

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
        return inflater.inflate(R.layout.fragment_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        showFirstBtn = view.findViewById(R.id.showFirstBtn)

        showFirstBtn.setOnClickListener {
            val action = StudentFragmentDirections.actionStudentFragmentToBiologyCourse()
            view.findNavController().navigate(action)
        }

        super.onViewCreated(view, savedInstanceState)
    }

}


