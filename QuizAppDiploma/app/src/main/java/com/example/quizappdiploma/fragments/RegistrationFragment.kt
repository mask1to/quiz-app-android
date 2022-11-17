package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.DatabaseHandler
import com.example.quizappdiploma.entities.Student
import com.google.android.material.textfield.TextInputLayout

class RegistrationFragment : Fragment()
{

    private lateinit var nicknameField : TextInputLayout
    private lateinit var emailField : TextInputLayout
    private lateinit var firstPassword : TextInputLayout
    private lateinit var secondPassword : TextInputLayout
    private lateinit var registerButton : Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        parentFragment?.activity?.actionBar?.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.registration_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        nicknameField = view.findViewById(R.id.nickNameField)
        emailField = view.findViewById(R.id.emailRegisterField)
        firstPassword = view.findViewById(R.id.passwordRegisterField)
        secondPassword = view.findViewById(R.id.passwordRegisterField2)
        registerButton = view.findViewById(R.id.registerBtn2)

        registerButton.setOnClickListener {

            if(checkFields())
            {
                val db = DatabaseHandler(requireContext())
                val student = Student(nicknameField.editText?.text.toString(), firstPassword.editText?.text.toString(), emailField.editText?.text.toString(), 0, 0, 1)
                db.insertNewUser(student)
                Toast.makeText(requireContext(), "Registrácia bola úspešná", Toast.LENGTH_SHORT).show()
                val action = RegistrationFragmentDirections.actionRegistrationFragmentToWelcomeFragment()
                view.findNavController().navigate(action)
            }
        }
    }

    private fun checkFields() : Boolean
    {
        if(nicknameField.editText?.text.toString().isEmpty() || emailField.editText?.text.toString().isEmpty() ||
            firstPassword.editText?.text.toString().isEmpty() || secondPassword.editText?.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(),"Každé pole musí byť vyplnené", Toast.LENGTH_SHORT).show()
            return false
        }

        //TODO: check for password length & email regex

        if(!firstPassword.editText?.text.toString().equals(secondPassword.editText?.text.toString()))
        {
            Toast.makeText(requireContext(), "Heslá sa musia zhodovať", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}