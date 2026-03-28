package com.example.quizappdiploma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import com.example.quizappdiploma.databinding.RegistrationFragmentBinding
import com.example.quizappdiploma.fragments.viewmodels.UserViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.UserViewModelFactory

class RegistrationFragment : Fragment() {
    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private var selectedRole: String = "Student"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = MyDatabase.getDatabase(requireContext()).userDao()
        val repository = UserDataRepository(dao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            usermodel = userViewModel
        }

        setupRoleSelector()

        binding.registerBtn2.setOnClickListener {
            if (checkFields()) {
                val email = binding.emailRegisterField.editText?.text.toString().trim()
                val nickname = binding.nickNameField.editText?.text.toString().trim()
                val password = binding.passwordRegisterField.editText?.text.toString()

                val isStudent = if (selectedRole == "Student") 1 else 0
                val isLecturer = if (selectedRole == "Lecturer") 1 else 0
                val isAdmin = if (selectedRole == "Administrator") 1 else 0

                val newUser = UserModel(null, email, nickname, password, isAdmin, isLecturer, isStudent)
                userViewModel.insertUser(newUser)
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registrationFragment_to_welcomeFragment)
            }
        }

        binding.backToLoginBtn.setOnClickListener {
            findNavController().navigateUp()
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
        card.setBackgroundResource(if (selected) R.drawable.bg_role_selected else R.drawable.bg_role_unselected)
        val tint = ContextCompat.getColorStateList(requireContext(), if (selected) R.color.md_primary else R.color.md_outline)
        icon.imageTintList = tint
        label.setTextColor(ContextCompat.getColor(requireContext(), if (selected) R.color.md_primary else R.color.md_outline))
    }

    private fun checkFields(): Boolean {
        val nickname = binding.nickNameField.editText?.text.toString()
        val email = binding.emailRegisterField.editText?.text.toString().trim()
        val password = binding.passwordRegisterField.editText?.text.toString()
        val confirm = binding.passwordRegisterField2.editText?.text.toString()

        if (nickname.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 8) {
            Toast.makeText(requireContext(), "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirm) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
