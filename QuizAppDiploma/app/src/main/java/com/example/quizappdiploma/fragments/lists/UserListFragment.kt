package com.example.quizappdiploma.fragments.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.lists.UserListAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import com.example.quizappdiploma.databinding.FragmentUserListBinding
import com.example.quizappdiploma.fragments.viewmodels.UserViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.UserViewModelFactory
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = MyDatabase.getDatabase(requireContext()).userDao()
        val repository = UserDataRepository(dao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]

        adapter = UserListAdapter(
            onEdit = { user -> showUserDialog(user) },
            onDelete = { user -> showDeleteConfirm(user) }
        )
        binding.userList.layoutManager = LinearLayoutManager(requireContext())
        binding.userList.adapter = adapter

        userViewModel.getUsers().observe(viewLifecycleOwner) { users ->
            adapter.userData = users
            binding.emptyState.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fab.setOnClickListener { showUserDialog(null) }
    }

    private fun showUserDialog(existing: UserModel?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_user, null)
        val usernameLayout = dialogView.findViewById<TextInputLayout>(R.id.usernameLayout)
        val emailLayout = dialogView.findViewById<TextInputLayout>(R.id.emailLayout)
        val passwordLayout = dialogView.findViewById<TextInputLayout>(R.id.passwordLayout)
        val roleGroup = dialogView.findViewById<ChipGroup>(R.id.roleChipGroup)

        if (existing != null) {
            usernameLayout.editText?.setText(existing.username)
            emailLayout.editText?.setText(existing.email)
            passwordLayout.editText?.setText(existing.password)
            when {
                existing.isAdmin == 1 -> roleGroup.check(R.id.chipAdmin)
                existing.isLecturer == 1 -> roleGroup.check(R.id.chipLecturer)
                else -> roleGroup.check(R.id.chipStudent)
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (existing == null) "Add User" else "Edit User")
            .setView(dialogView)
            .setPositiveButton(if (existing == null) "Add" else "Update") { _, _ ->
                val username = usernameLayout.editText?.text.toString().trim()
                val email = emailLayout.editText?.text.toString().trim()
                val password = passwordLayout.editText?.text.toString()

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val isAdmin = if (roleGroup.checkedChipId == R.id.chipAdmin) 1 else 0
                val isLecturer = if (roleGroup.checkedChipId == R.id.chipLecturer) 1 else 0
                val isStudent = if (roleGroup.checkedChipId == R.id.chipStudent) 1 else 0

                val user = UserModel(existing?.id, email, username, password, isAdmin, isLecturer, isStudent)
                if (existing == null) userViewModel.insertUser(user) else userViewModel.updateUser(user)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirm(user: UserModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete User")
            .setMessage("Delete user \"${user.username}\" (${user.email})?")
            .setPositiveButton("Delete") { _, _ -> userViewModel.deleteUser(user) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
