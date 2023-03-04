package com.example.quizappdiploma.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.CourseAdapter
import com.example.quizappdiploma.databinding.FragmentStudentBinding
import com.example.quizappdiploma.fragments.courses.CourseFragment
import com.example.quizappdiploma.preferences.PreferenceManager
import com.google.android.material.navigation.NavigationView

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

        replaceFragment(ProfileFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.logout -> logout()
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.courses -> replaceFragment(CourseFragment())
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


