package com.example.quizappdiploma.fragments.entities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import com.example.quizappdiploma.databinding.FragmentAdminBinding
import com.example.quizappdiploma.fragments.lists.CourseListFragment
import com.example.quizappdiploma.fragments.lists.UserListFragment
import com.example.quizappdiploma.fragments.viewmodels.*
import com.example.quizappdiploma.fragments.viewmodels.factory.*
import com.example.quizappdiploma.preferences.PreferenceManager
import kotlinx.android.synthetic.main.custom_dialog_add_course.*
import kotlinx.android.synthetic.main.custom_dialog_add_user.*

class AdminFragment : Fragment()
{

    private var _binding : FragmentAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    private val usersList = UserListFragment()
    private val coursesList = CourseListFragment()

    private lateinit var userViewModel : UserViewModel
    private lateinit var courseViewModel : CourseViewModel

    private var currentList: String = "users"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        //checkLoginStatus()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                // Save the current navigation state
                val navController = findNavController()
                val navState = navController.saveState()

                // Remove all the previous fragments from the back stack
                //TODO: change the fragment
                navController.popBackStack(R.id.courseFragment, true)

                // Minimize the app
                requireActivity().moveTaskToBack(true)

                // Restore the navigation state when the app is resumed
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val userDao = MyDatabase.getDatabase(requireContext()).userDao()
        val courseDao = MyDatabase.getDatabase(requireContext()).courseDao()

        val userRepository = UserDataRepository(userDao)
        val courseRepository = CourseDataRepository(courseDao)

        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(courseRepository))[CourseViewModel::class.java]

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.usersRadioButton -> currentList = "user"
                R.id.coursesRadioButton -> currentList = "course"
            }
        }

        binding.usersRadioButton.isChecked = true

        // Set up initial fragment to be displayed
        switchFragment(usersList)

        // Set up navigation buttons
        binding.usersButton.setOnClickListener {
            switchFragment(usersList)
        }
        binding.coursesButton.setOnClickListener {
            switchFragment(coursesList)
        }

        binding.addButton.setOnClickListener {
            showDialog("add", currentList)
        }

        binding.updateButton.setOnClickListener {
            showDialog("update", currentList)
        }

        binding.removeButton.setOnClickListener {
            showDialog("remove", currentList)
        }
    }

    private fun switchFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    private fun showDialog(action: String, listType: String)
    {
        val layoutId = when (listType)
        {
            "user" -> when (action)
            {
                "add" -> R.layout.custom_dialog_add_user
                "update" -> R.layout.custom_dialog_update_user
                "remove" -> R.layout.custom_dialog_remove_user
                else -> throw IllegalArgumentException("Invalid action")
            }
            "course" -> when (action)
            {
                "add" -> R.layout.custom_dialog_add_course
                "update" -> R.layout.custom_dialog_update_course
                "remove" -> R.layout.custom_dialog_remove_course
                else -> throw IllegalArgumentException("Invalid action")
            }
            else -> throw IllegalArgumentException("Invalid listType")
        }

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutId)

        val title = dialog.findViewById<TextView>(R.id.dialogTitle)
        title.text = "${action.capitalize()} ${listType.capitalize()}"

        val positiveButton = dialog.findViewById<Button>(R.id.dialogPositiveButton)

        positiveButton.text = action.capitalize()
        positiveButton.setOnClickListener {
            when (action)
            {
                "add" -> {
                    if (listType == "user")
                    {
                        val emailField = dialog.findViewById<EditText>(R.id.firstDialogField)
                        val usernameField = dialog.findViewById<EditText>(R.id.secondDialogField)
                        val passwordField = dialog.findViewById<EditText>(R.id.thirdDialogField)
                        val user = UserModel(
                            id = null,
                            email = emailField?.text.toString(),
                            username = usernameField?.text.toString(),
                            password = passwordField?.text.toString(),
                            isAdmin = 0,
                            isLecturer = 0,
                            isStudent = 1
                        )
                        if(checkUserFields())
                        {
                            userViewModel.insertUser(user)
                        }
                    }
                    else if (listType == "course")
                    {
                        val courseName = dialog.findViewById<EditText>(R.id.courseDialogField)
                        val course = CourseModel(
                            id = null,
                            courseName = courseName?.text.toString()
                        )
                        courseViewModel.addCourse(course)
                    }
                }
                "update" -> {
                    if (listType == "user")
                    {
                        val currentEmailField = dialog.findViewById<EditText>(R.id.currentEmailField)
                        val currentUsernameField = dialog.findViewById<EditText>(R.id.currentUsernameField)
                        val newEmailField = dialog.findViewById<EditText>(R.id.newEmailField)
                        val newUsernameField = dialog.findViewById<EditText>(R.id.newUsernameField)
                        val newPasswordField = dialog.findViewById<EditText>(R.id.newPasswordField)

                        userViewModel.getUserByUsernameAndEmail(currentUsernameField?.text.toString(),
                            currentEmailField?.text.toString()).observeOnce(viewLifecycleOwner) { user ->
                            if(user != null)
                            {
                                val currUser = user.first()
                                Log.d("UpdateUser", "Current user: $currUser")
                                val updatedUser = UserModel(
                                    id = currUser.id,
                                    email = newEmailField?.text.toString(),
                                    username = newUsernameField?.text.toString(),
                                    password = newPasswordField?.text.toString(),
                                    isAdmin = 0,
                                    isLecturer = 0,
                                    isStudent = 1
                                )
                                Log.d("UpdateUser", "Updated user: $updatedUser")
                                userViewModel.updateUser(updatedUser)
                            }
                        }
                    }
                    else if (listType == "course")
                    {
                        val currentCourseName = dialog.findViewById<EditText>(R.id.currentCourseNameField)
                        val newCourseName = dialog.findViewById<EditText>(R.id.newCourseNameField)

                        courseViewModel.getCourseByName(currentCourseName?.text.toString()).observeOnce(viewLifecycleOwner){ course ->
                            if(course != null)
                            {
                                val currCourse = course.first()
                                val updatedCourse = CourseModel(
                                    id = currCourse.id,
                                    courseName = newCourseName?.text.toString()
                                )
                                courseViewModel.updateCourse(updatedCourse)
                            }
                        }
                    }
                }
                "remove" -> {
                    if (listType == "user")
                    {
                        val userEmail = dialog.findViewById<EditText>(R.id.removeUserField)
                        userViewModel.getUserByEmail(userEmail?.text.toString()).observeOnce(viewLifecycleOwner){user ->
                            if(user != null)
                            {
                                val currUser = user.first()
                                val removedUser = UserModel(
                                    id = currUser.id,
                                    email = userEmail?.text.toString(),
                                    username = null,
                                    password = null,
                                    isAdmin = null,
                                    isLecturer = null,
                                    isStudent = null
                                )
                                userViewModel.deleteUser(removedUser)
                            }
                        }
                    }
                    else if (listType == "course")
                    {
                        val courseName = dialog.findViewById<EditText>(R.id.removeCourseField)
                        courseViewModel.getCourseByName(courseName?.text.toString()).observeOnce(viewLifecycleOwner){course ->
                            if(course != null)
                            {
                                val currCourse = course.first()
                                val removedCourse = CourseModel(
                                    id = currCourse.id,
                                    courseName = courseName?.text.toString()
                                )
                                courseViewModel.deleteCourse(removedCourse)
                            }
                        }
                    }
                }
            }
            dialog.dismiss()
        }

        val negativeButton = dialog.findViewById<Button>(R.id.dialogNegativeButton)
        negativeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    private fun checkUserFields(): Boolean {
        if (firstDialogField?.text.toString().isEmpty()
            || secondDialogField?.text.toString().isEmpty()
            || thirdDialogField?.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "Každé pole musí byť vyplnené", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check for valid email format using a regex pattern
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!firstDialogField?.text.toString().trim().matches(emailPattern.toRegex())) {
            Toast.makeText(requireContext(), "Neplatný formát e-mailu", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check for password length
        if (thirdDialogField?.text.toString().length < 8) {
            Toast.makeText(requireContext(), "Heslo musí obsahovať aspoň 8 znakov", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkCourseFields(): Boolean{

        if(courseDialogField?.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "This field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    //TODO: CHECKERS FOR FIELDS
}