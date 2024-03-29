package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.databinding.WelcomeFragmentBinding
import com.example.quizappdiploma.fragments.viewmodels.UserViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.UserViewModelFactory
import com.example.quizappdiploma.preferences.PreferenceManager
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment() {
    private var _binding: WelcomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var textField: TextInputLayout
    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var userViewModel: UserViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        //preferenceManager.logout()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textField = binding.menu
        registerButton = binding.registerBtn
        loginButton = binding.loginBtn
        emailField = binding.emailField
        passwordField = binding.passwordField

        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val navState = navController.saveState()
                navController.popBackStack(R.id.studentFragment, true)
                requireActivity().moveTaskToBack(true)
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        checkLogin()

        val dao = MyDatabase.getDatabase(requireContext()).userDao()
        val repository = UserDataRepository(dao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]

        val items = listOf("Student", "Lecturer", "Administrator")
        val adapter = ArrayAdapter(requireContext(), R.layout.entity_dropdown_item, items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        loginButton.setOnClickListener {
            val emailInput = emailField.editText?.text.toString()
            val passwordInput = passwordField.editText?.text.toString()
            val role = textField.editText?.text.toString()

            // Check if user exists in the database
            viewLifecycleOwner.lifecycleScope.launch {
                val user = userViewModel.getUserByEmailAndPassword(emailInput, passwordInput)
                if (user != null && role.isNotEmpty())
                {
                    // Logged in successfully
                    preferenceManager.saveUser(user)

                    if(user.isStudent == 1 && user.isAdmin == 0 && user.isLecturer == 0 && role == "Student")
                    {
                        val action = WelcomeFragmentDirections.actionWelcomeFragmentToStudentFragment()
                        findNavController().navigate(action)
                    }
                    else if(user.isLecturer == 1 && user.isAdmin == 0 && user.isStudent == 0 && role == "Lecturer")
                    {
                        val action = WelcomeFragmentDirections.actionWelcomeFragmentToLecturerFragment()
                        findNavController().navigate(action)
                    }
                    else if(user.isAdmin == 1 && user.isLecturer == 0 && user.isStudent == 0 && role == "Administrator")
                    {
                        val action = WelcomeFragmentDirections.actionWelcomeFragmentToAdminFragment()
                        findNavController().navigate(action)
                    }

                }
                else
                {
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
        registerButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationFragment()
            view.findNavController().navigate(action)
        }
    }

    private fun checkLogin() {
        val loggedInUser = preferenceManager.getLoggedInUser()
        Log.d("checkLogin", "Logged-in user: $loggedInUser")
        if (loggedInUser != null) {
            if (loggedInUser.isStudent == 1 && loggedInUser.isAdmin == 0 && loggedInUser.isLecturer == 0) {
                Log.d("checkLogin", "Navigating to StudentFragment")
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToStudentFragment()
                findNavController().navigate(action)
            } else if (loggedInUser.isLecturer == 1 && loggedInUser.isAdmin == 0 && loggedInUser.isStudent == 0) {
                Log.d("checkLogin", "Navigating to LecturerFragment")
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToLecturerFragment()
                findNavController().navigate(action)
            } else if (loggedInUser.isAdmin == 1 && loggedInUser.isLecturer == 0 && loggedInUser.isStudent == 0) {
                Log.d("checkLogin", "Navigating to AdminFragment")
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToAdminFragment()
                findNavController().navigate(action)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}