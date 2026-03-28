package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment() {
    private var _binding: WelcomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var preferenceManager: PreferenceManager

    private var selectedRole: String = "Student"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().moveTaskToBack(true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        checkLogin()

        val dao = MyDatabase.getDatabase(requireContext()).userDao()
        val repository = UserDataRepository(dao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]

        setupRoleSelector()

        binding.loginBtn.setOnClickListener {
            val emailInput = binding.emailField.editText?.text.toString().trim()
            val passwordInput = binding.passwordField.editText?.text.toString()

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val user = userViewModel.getUserByEmailAndPassword(emailInput, passwordInput)
                if (user != null) {
                    preferenceManager.saveUser(user)
                    when {
                        user.isStudent == 1 && selectedRole == "Student" ->
                            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToStudentFragment())
                        user.isLecturer == 1 && selectedRole == "Lecturer" ->
                            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLecturerFragment())
                        user.isAdmin == 1 && selectedRole == "Administrator" ->
                            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToAdminFragment())
                        else ->
                            Toast.makeText(requireContext(), "Role mismatch or invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registerBtn.setOnClickListener {
            view.findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationFragment())
        }
    }

    private fun setupRoleSelector() {
        selectRole("Student")

        binding.roleStudent.setOnClickListener { selectRole("Student") }
        binding.roleLecturer.setOnClickListener { selectRole("Lecturer") }
        binding.roleAdmin.setOnClickListener { selectRole("Administrator") }
    }

    private fun selectRole(role: String) {
        selectedRole = role

        setRoleCardState(binding.roleStudent, binding.iconStudent, binding.labelStudent, role == "Student")
        setRoleCardState(binding.roleLecturer, binding.iconLecturer, binding.labelLecturer, role == "Lecturer")
        setRoleCardState(binding.roleAdmin, binding.iconAdmin, binding.labelAdmin, role == "Administrator")
    }

    private fun setRoleCardState(card: LinearLayout, icon: ImageView, label: TextView, selected: Boolean) {
        val bg = if (selected) R.drawable.bg_role_selected else R.drawable.bg_role_unselected
        val tintColor = if (selected) R.color.md_primary else R.color.md_outline
        val textColor = if (selected) R.color.md_primary else R.color.md_outline

        card.setBackgroundResource(bg)
        icon.imageTintList = ContextCompat.getColorStateList(requireContext(), tintColor)
        label.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    private fun checkLogin() {
        val user = preferenceManager.getLoggedInUser() ?: return
        Log.d("WelcomeFragment", "Auto-login: $user")
        when {
            user.isStudent == 1 && user.isAdmin == 0 && user.isLecturer == 0 ->
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToStudentFragment())
            user.isLecturer == 1 && user.isAdmin == 0 && user.isStudent == 0 ->
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLecturerFragment())
            user.isAdmin == 1 && user.isLecturer == 0 && user.isStudent == 0 ->
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToAdminFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
