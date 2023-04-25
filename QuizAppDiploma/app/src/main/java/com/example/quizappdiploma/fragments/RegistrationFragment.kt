package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import com.example.quizappdiploma.databinding.RegistrationFragmentBinding
import com.example.quizappdiploma.fragments.viewmodels.QuizQuestionViewModel
import com.example.quizappdiploma.fragments.viewmodels.UserViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizQuestionViewModelFactory
import com.example.quizappdiploma.fragments.viewmodels.factory.UserViewModelFactory
import com.google.android.material.textfield.TextInputLayout

class RegistrationFragment : Fragment()
{
    private var _binding : RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var nicknameField : TextInputLayout
    private lateinit var emailField : TextInputLayout
    private lateinit var firstPassword : TextInputLayout
    private lateinit var secondPassword : TextInputLayout
    private lateinit var registerButton : Button
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        parentFragment?.activity?.actionBar?.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        nicknameField = view.findViewById(R.id.nickNameField)
        emailField = view.findViewById(R.id.emailRegisterField)
        firstPassword = view.findViewById(R.id.passwordRegisterField)
        secondPassword = view.findViewById(R.id.passwordRegisterField2)
        registerButton = view.findViewById(R.id.registerBtn2)

        val dao = MyDatabase.getDatabase(requireContext()).userDao()
        val repository = UserDataRepository(dao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            usermodel = userViewModel
        }

        registerButton.setOnClickListener {
            val email = emailField.editText?.text.toString()
            val nickname = nicknameField.editText?.text.toString()
            val password = firstPassword.editText?.text.toString()

            if(checkFields())
            {
                val student = UserModel(null, email, nickname, password, 0, 0, 1)
                userViewModel.insertUser(student)
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registrationFragment_to_welcomeFragment)
            }

        }
    }
    private fun checkFields(): Boolean {
        if (nicknameField.editText?.text.toString().isEmpty() || emailField.editText?.text.toString().isEmpty() ||
            firstPassword.editText?.text.toString().isEmpty() || secondPassword.editText?.text.toString().isEmpty()
        ) {
            Toast.makeText(requireContext(), "Každé pole musí byť vyplnené", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check for valid email format using a regex pattern
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!emailField.editText?.text.toString().trim().matches(emailPattern.toRegex())) {
            Toast.makeText(requireContext(), "Neplatný formát e-mailu", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check for password length
        if (firstPassword.editText?.text.toString().length < 8) {
            Toast.makeText(requireContext(), "Heslo musí obsahovať aspoň 8 znakov", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!firstPassword.editText?.text.toString().equals(secondPassword.editText?.text.toString())) {
            Toast.makeText(requireContext(), "Heslá sa musia zhodovať", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}