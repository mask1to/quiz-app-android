package com.example.quizappdiploma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.quizappdiploma.preferences.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()
{
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?)
    {
        preferenceManager = PreferenceManager(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.nav_view)
            .setupWithNavController(navController)
    }
    override fun onSupportNavigateUp(): Boolean
    {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

}