package com.example.quizappdiploma.fragments

import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.CourseAdapter
import com.example.quizappdiploma.databinding.FragmentStudentBinding
import com.example.quizappdiploma.preferences.PreferenceManager
import com.google.android.material.navigation.NavigationView

class StudentFragment : Fragment()
{

    private var _binding : FragmentStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var courseBtn : Button
    private lateinit var logoutBtn : Button
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var toggle : ActionBarDrawerToggle


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
        /*courseBtn = binding.courseButton
        logoutBtn = binding.logoutButton

        logoutBtn.setOnClickListener {
            logout()
        }

        courseBtn.setOnClickListener {
            val action = StudentFragmentDirections.actionStudentFragmentToCourseFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }*/

        /*drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        toolbar = binding.toolbar
        //(activity as AppCompatActivity).setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/

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


