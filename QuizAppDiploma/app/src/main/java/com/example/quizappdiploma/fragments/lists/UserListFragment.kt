package com.example.quizappdiploma.fragments.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappdiploma.adapters.lists.UserListAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.databinding.FragmentUserListBinding
import com.example.quizappdiploma.fragments.viewmodels.UserViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_user_list.*

class UserListFragment : Fragment()
{
    private var _binding : FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val dao = MyDatabase.getDatabase(requireContext()).userDao()
        val repository = UserDataRepository(dao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]


        val adapter = UserListAdapter()
        userList.layoutManager = LinearLayoutManager(requireContext())
        userList.adapter = adapter

        userViewModel.getUsers().observe(viewLifecycleOwner) { users ->
            adapter.setData(users)
        }
    }
}