package com.example.quizappdiploma.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentAdminBinding
import com.example.quizappdiploma.databinding.FragmentStudentBinding
import com.example.quizappdiploma.preferences.PreferenceManager

class AdminFragment : Fragment()
{

    private var _binding : FragmentAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        checkLoginStatus()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkLoginStatus()
    {
        if(preferenceManager.isLogin() == false)
        {
            val action = StudentFragmentDirections.actionStudentFragmentToWelcomeFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    private fun logout()
    {
        preferenceManager.removeData()
        val action = StudentFragmentDirections.actionStudentFragmentToWelcomeFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

}