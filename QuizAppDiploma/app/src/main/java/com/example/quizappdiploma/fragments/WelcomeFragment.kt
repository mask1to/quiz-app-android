package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.quizappdiploma.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout


class WelcomeFragment : Fragment()
{
    private lateinit var textField : TextInputLayout
    private lateinit var emailField : TextInputLayout
    private lateinit var passwordField : TextInputLayout
    private lateinit var registerButton : Button
    private lateinit var loginButton : Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.welcome_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        textField = view.findViewById(R.id.menu)
        registerButton = view.findViewById(R.id.registerBtn)
        loginButton = view.findViewById(R.id.loginBtn)
        emailField = view.findViewById(R.id.emailField)
        passwordField = view.findViewById(R.id.passwordField)

        val items = listOf("Študent", "Lektor", "Administrátor")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        loginButton.setOnClickListener {
            //TODO()
            val emailInput = emailField.editText?.text.toString()
            val passwordInput = passwordField.editText?.text.toString()
            /**
             * Student logged in
             **/
            if(textField.editText?.text.toString() == "Študent")
            {
                if(emailInput == "student" && passwordInput == "student")
                {
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

}