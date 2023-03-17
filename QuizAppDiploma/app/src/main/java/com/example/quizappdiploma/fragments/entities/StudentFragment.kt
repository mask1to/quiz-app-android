package com.example.quizappdiploma.fragments.entities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentStudentBinding
import com.example.quizappdiploma.fragments.ProfileFragment
import com.example.quizappdiploma.fragments.StatsFragment
import com.example.quizappdiploma.preferences.PreferenceManager

class StudentFragment : Fragment()
{

    private var _binding : FragmentStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        _binding = FragmentStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        checkLoginStatus()

        replaceFragment(ProfileFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.logout -> logout()
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.stats -> replaceFragment(StatsFragment())
                else ->
                {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment)
    {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun checkLoginStatus()
    {
        if(preferenceManager.isLogin() == false)
        {
            val action = StudentFragmentDirections.actionStudentFragmentToWelcomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun logout()
    {
        preferenceManager.removeData()
        val action =
            StudentFragmentDirections.actionStudentFragmentToWelcomeFragment()
        findNavController().navigate(action)
    }

}


