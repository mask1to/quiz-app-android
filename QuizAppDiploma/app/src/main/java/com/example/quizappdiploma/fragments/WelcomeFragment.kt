package com.example.quizappdiploma.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.databinding.WelcomeFragmentBinding
import com.example.quizappdiploma.fragments.viewmodels.UserViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.UserViewModelFactory
import com.example.quizappdiploma.preferences.PreferenceManager
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch


class WelcomeFragment : Fragment()
{
    private var _binding : WelcomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var textField : TextInputLayout
    private lateinit var emailField : TextInputLayout
    private lateinit var passwordField : TextInputLayout
    private lateinit var registerButton : Button
    private lateinit var loginButton : Button
    private lateinit var userViewModel: UserViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        _binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        checkLogin()

        textField = binding.menu
        registerButton = binding.registerBtn
        loginButton = binding.loginBtn
        emailField = binding.emailField
        passwordField = binding.passwordField

        val dao = MyDatabase.getDatabase(requireContext()).userDao()
        val repository = UserDataRepository(dao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]

        val items = listOf("Študent", "Lektor", "Administrátor")
        val adapter = ArrayAdapter(requireContext(), R.layout.entity_dropdown_item, items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        loginButton.setOnClickListener {
            val emailInput = emailField.editText?.text.toString()
            val passwordInput = passwordField.editText?.text.toString()
            val role = textField.editText?.text.toString()

            // Check if user exists in the database
            viewLifecycleOwner.lifecycleScope.launch {
                val user = userViewModel.getUserByEmailAndPassword(emailInput, passwordInput)
                if (user != null && role.isNotEmpty()) {
                    // Logged in successfully
                    preferenceManager.setLogin(true)
                    //preferenceManager.setUsername(emailInput)
                    preferenceManager.setLoggedInUser(user)

                    val action = when (role) {
                        "Študent" -> WelcomeFragmentDirections.actionWelcomeFragmentToStudentFragment()
                        "Lektor" -> WelcomeFragmentDirections.actionWelcomeFragmentToLecturerFragment()
                        "Administrátor" -> WelcomeFragmentDirections.actionWelcomeFragmentToAdminFragment2()
                        else -> null
                    }

                    action?.let {
                        view.findNavController().navigate(it)
                    } ?: run {
                        // Handle invalid role
                        Toast.makeText(requireContext(), "Invalid role", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle invalid email or password
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationFragment()
            view.findNavController().navigate(action)
        }

    }

    private fun checkLogin()
    {
        if(preferenceManager.isLogin()!!)
        {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToStudentFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

}