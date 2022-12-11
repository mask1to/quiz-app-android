package com.example.quizappdiploma.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.WelcomeFragmentBinding
import com.example.quizappdiploma.preferences.PreferenceManager
import com.google.android.material.textfield.TextInputLayout


class WelcomeFragment : Fragment()
{
    private var _binding : WelcomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var textField : TextInputLayout
    private lateinit var emailField : TextInputLayout
    private lateinit var passwordField : TextInputLayout
    private lateinit var registerButton : Button
    private lateinit var loginButton : Button
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
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

        val items = listOf("Študent", "Lektor", "Administrátor")
        val adapter = ArrayAdapter(requireContext(), R.layout.entity_dropdown_item, items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        loginButton.setOnClickListener {
            val emailInput = emailField.editText?.text.toString()
            val passwordInput = passwordField.editText?.text.toString()

            //check if user exists

            /**
             * Student logged in
             **/
            if(textField.editText?.text.toString() == "Študent")
            {
                if(emailInput == "student" && passwordInput == "student")
                {
                    preferenceManager.setLogin(true)
                    preferenceManager.setUsername(emailInput)
                    val action = WelcomeFragmentDirections.actionWelcomeFragmentToStudentFragment()
                    view.findNavController().navigate(action)
                }
            }
            else if(textField.editText?.text.toString() == "Lektor")
            {
                /**
                 *  Lecturer logged in
                 **/
                if(emailInput == "lecturer" && passwordInput == "lecturer")
                {
                    preferenceManager.setLogin(true)
                    preferenceManager.setUsername(emailInput)

                    val action = WelcomeFragmentDirections.actionWelcomeFragmentToLecturerFragment()
                    view.findNavController().navigate(action)
                }
            }
            else if(textField.editText?.text.toString() == "Administrátor")
            {
                /**
                 * Admin logged in
                 **/
                if(emailInput == "admin" && passwordInput == "admin")
                {
                    preferenceManager.setLogin(true)
                    preferenceManager.setUsername(emailInput)
                    val action = WelcomeFragmentDirections.actionWelcomeFragmentToAdminFragment2()
                    view.findNavController().navigate(action)
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