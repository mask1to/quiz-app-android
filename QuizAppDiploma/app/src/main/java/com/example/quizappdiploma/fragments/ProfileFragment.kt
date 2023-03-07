package com.example.quizappdiploma.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentProfileBinding
import com.example.quizappdiploma.databinding.FragmentStudentBinding

class ProfileFragment : Fragment()
{

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileImage : ImageView
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImage = binding.imageView

        binding.courseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_studentFragment_to_courseFragment)
        }
    }
}