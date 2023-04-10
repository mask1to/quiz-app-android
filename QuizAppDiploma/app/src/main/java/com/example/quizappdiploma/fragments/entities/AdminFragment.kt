package com.example.quizappdiploma.fragments.entities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import com.example.quizappdiploma.databinding.FragmentAdminBinding
import com.example.quizappdiploma.fragments.lists.CourseListFragment
import com.example.quizappdiploma.fragments.lists.UserListFragment
import com.example.quizappdiploma.fragments.viewmodels.*
import com.example.quizappdiploma.fragments.viewmodels.factory.*
import com.example.quizappdiploma.preferences.PreferenceManager
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog_add_course.*
import kotlinx.android.synthetic.main.custom_dialog_add_user.*
import kotlinx.android.synthetic.main.custom_dialog_remove_course.*
import kotlinx.android.synthetic.main.custom_dialog_remove_user.*
import kotlinx.android.synthetic.main.custom_dialog_update_course.*
import kotlinx.android.synthetic.main.custom_dialog_update_user.*
import java.util.regex.Pattern

class AdminFragment : Fragment()
{

    private var _binding : FragmentAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager

    private val usersList = UserListFragment()
    private val coursesList = CourseListFragment()

    private lateinit var userViewModel : UserViewModel
    private lateinit var courseViewModel : CourseViewModel
    private lateinit var lectureViewModel : LectureViewModel

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
        val lectureDao = MyDatabase.getDatabase(requireContext()).lectureDao()

        val userRepository = UserDataRepository(userDao)
        val courseRepository = CourseDataRepository(courseDao)
        val lectureRepository = LectureDataRepository(lectureDao)

        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(courseRepository))[CourseViewModel::class.java]
        lectureViewModel = ViewModelProvider(this, LectureViewModelFactory(lectureRepository))[LectureViewModel::class.java]

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.usersRadioButton -> currentList = "user"
                R.id.coursesRadioButton -> currentList = "course"
                R.id.lecturesRadioButton -> currentList = "lecture"
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
        binding.logoutButton.setOnClickListener {
            logout()
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
            "lecture" -> when(action)
            {
                "add" -> R.layout.custom_dialog_add_lecture
                "update" -> R.layout.custom_dialog_update_lecture
                "remove" -> R.layout.custom_dialog_remove_lecture
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

        val courseMenu = dialog.findViewById<TextInputLayout>(R.id.courseMenu)
        val lectureMenu = dialog.findViewById<TextInputLayout>(R.id.lectureMenu)
        val imageUrlField = dialog.findViewById<TextInputLayout>(R.id.imageUrlField)
        val newLectureDesc = dialog.findViewById<TextInputLayout>(R.id.newLectureDescDialogField)

        if (courseMenu != null)
        {
            val adapter = ArrayAdapter(requireContext(), R.layout.entity_dropdown_item, mutableListOf<String>())
            (courseMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            courseViewModel.getCourseNames().observeOnce(viewLifecycleOwner)
            { courses ->
                val courseNames = courses.map { it.courseName }
                val courseIds = courses.map { it.id }

                adapter.clear()
                adapter.addAll(courseNames)
                val courseAutoCompleteTextView = (courseMenu.editText as? AutoCompleteTextView)
                courseAutoCompleteTextView?.setAdapter(adapter)

                courseAutoCompleteTextView?.setOnItemClickListener{ parent, _, position, _ ->
                    val selectedCourseId = courseIds[position]

                    if (selectedCourseId != null && lectureMenu != null)
                    {
                        val lectureAdapter = ArrayAdapter(requireContext(), R.layout.entity_dropdown_item, mutableListOf<String>())
                        (lectureMenu.editText as? AutoCompleteTextView)?.setAdapter(lectureAdapter)

                        lectureViewModel.getLectureNameByCourseId(selectedCourseId).observeOnce(viewLifecycleOwner)
                        { lectures ->
                            val lectureNames = lectures.mapNotNull { it.lectureName } // Filter out null values

                            lectureAdapter.clear()
                            lectureAdapter.addAll(lectureNames)
                            val lectureAutoCompleteTextView = (lectureMenu.editText as? AutoCompleteTextView)
                            lectureAutoCompleteTextView?.setAdapter(lectureAdapter)

                            lectureAutoCompleteTextView?.setOnItemClickListener { _, _, position, _ ->
                                val selectedLectureName = lectureAutoCompleteTextView.adapter.getItem(position).toString()

                                lectureViewModel.getLectureDescByLectureName(selectedLectureName).observeOnce(viewLifecycleOwner)
                                { lecture ->
                                    if (lecture != null)
                                    {
                                        if (lecture.isNotEmpty())
                                        {
                                            val currLecture = lecture.first()
                                            Log.d("currLecture: ", currLecture.toString())
                                            if (newLectureDesc != null) {
                                                newLectureDesc.editText?.setText(currLecture.lectureDescription)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        positiveButton.text = action.capitalize()
        positiveButton.setOnClickListener {
            when (action)
            {
                "add" ->
                {
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
                        if(checkUserFields(emailField, usernameField, passwordField))
                        {
                            userViewModel.insertUser(user)
                            showConfirmationDialog("User has been added")
                        }
                    }
                    else if (listType == "course")
                    {
                        val courseName = dialog.findViewById<EditText>(R.id.courseDialogField)
                        val course = CourseModel(
                            id = null,
                            courseName = courseName?.text.toString()
                        )
                        if(checkCourseFields(courseName))
                        {
                            courseViewModel.addCourse(course)
                            showConfirmationDialog("Course has been added")
                        }
                    }
                    else
                    {
                        val lectureName = dialog.findViewById<TextInputLayout>(R.id.lectureNameDialogField)
                        val lectureDesc = dialog.findViewById<TextInputLayout>(R.id.lectureDescDialogField)
                        val course = courseMenu.editText?.text.toString()

                        courseViewModel.getCourseIdByName(course).observeOnce(viewLifecycleOwner){ courseId ->
                            if(courseId != null)
                            {
                                val url = imageUrlField?.editText?.text.toString()
                                if(isValidUrl(url))
                                {
                                    if(courseId.isNotEmpty())
                                    {
                                        val cId = courseId.first()
                                        val lecture = LectureModel(
                                            id = null,
                                            course_id = cId.id,
                                            image_path = url,
                                            lectureName = lectureName?.editText?.text.toString(),
                                            lectureDescription = lectureDesc?.editText?.text.toString()
                                        )
                                        if(checkLectureFields(lectureName, lectureDesc))
                                        {
                                            lectureViewModel.addLecture(lecture)
                                            showConfirmationDialog("Lecture has been added")
                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(requireContext(), "Please enter valid URL", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }
                }
                "update" ->
                {
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
                                if(user.isNotEmpty())
                                {
                                    val currUser = user.first()
                                    val updatedUser = UserModel(
                                        id = currUser.id,
                                        email = newEmailField?.text.toString(),
                                        username = newUsernameField?.text.toString(),
                                        password = newPasswordField?.text.toString(),
                                        isAdmin = 0,
                                        isLecturer = 0,
                                        isStudent = 1
                                    )
                                    if(checkUpdatedUserFields(currentEmailField, newEmailField, currentUsernameField, newUsernameField, newPasswordField))
                                    {
                                        userViewModel.updateUser(updatedUser)
                                        showConfirmationDialog("User has been updated")
                                    }
                                }
                                else
                                {
                                    Toast.makeText(requireContext(), "Fill in fields, please", Toast.LENGTH_SHORT).show()
                                }
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
                                if(course.isNotEmpty())
                                {
                                    val currCourse = course.first()
                                    val updatedCourse = CourseModel(
                                        id = currCourse.id,
                                        courseName = newCourseName?.text.toString()
                                    )
                                    if(checkUpdatedCourseFields(currentCourseName, newCourseName))
                                    {
                                        courseViewModel.updateCourse(updatedCourse)
                                        showConfirmationDialog("Course has been updated")
                                    }
                                }
                                else
                                {
                                    Toast.makeText(requireContext(), "Fill in fields, please", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    else
                    {
                        val lectureName = lectureMenu.editText?.text.toString()

                        lectureViewModel.getLectureDescByLectureName(lectureName).observeOnce(viewLifecycleOwner){lecture ->
                            if(lecture !=null)
                            {
                                if(lecture.isNotEmpty())
                                {
                                    val currLecture = lecture.first()
                                    val updatedLecture = LectureModel(
                                        id = currLecture.id,
                                        course_id = currLecture.course_id,
                                        image_path = currLecture.image_path,
                                        lectureName = currLecture.lectureName,
                                        lectureDescription = newLectureDesc.editText?.text.toString()
                                    )

                                    lectureViewModel.updateLecture(updatedLecture)
                                    showConfirmationDialog("Lecture has been updated")
                                }
                            }
                        }
                    }
                }
                "remove" ->
                {
                    if (listType == "user")
                    {
                        val userEmail = dialog.findViewById<EditText>(R.id.removeUserField)
                        userViewModel.getUserByEmail(userEmail?.text.toString()).observeOnce(viewLifecycleOwner){user ->
                            if(user != null)
                            {
                                if(user.isNotEmpty())
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
                                    if(checkRemovedUserFields(userEmail))
                                    {
                                        userViewModel.deleteUser(removedUser)
                                        showConfirmationDialog("User has been deleted")
                                    }
                                }
                                else
                                {
                                    Toast.makeText(requireContext(), "Fill in the field, please", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    else if (listType == "course")
                    {
                        val courseName = dialog.findViewById<EditText>(R.id.removeCourseField)
                        courseViewModel.getCourseByName(courseName?.text.toString()).observeOnce(viewLifecycleOwner){course ->
                            if(course != null)
                            {
                                if(course.isNotEmpty())
                                {
                                    val currCourse = course.first()
                                    val removedCourse = CourseModel(
                                        id = currCourse.id,
                                        courseName = courseName?.text.toString()
                                    )
                                    if(checkRemovedCourseFields(courseName))
                                    {
                                        courseViewModel.deleteCourse(removedCourse)
                                        showConfirmationDialog("Course has been deleted")
                                    }
                                }
                                else
                                {
                                    Toast.makeText(requireContext(), "Fill in the field, please", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    else
                    {
                        val lectureName = lectureMenu.editText?.text.toString()
                        lectureViewModel.getLectureDescByLectureName(lectureName).observeOnce(viewLifecycleOwner){lecture ->
                            if(lecture != null)
                            {
                                if(lecture.isNotEmpty())
                                {
                                    val currLecture = lecture.first()
                                    val removedLecture = LectureModel(
                                        id = currLecture.id,
                                        course_id = currLecture.course_id,
                                        image_path = currLecture.image_path,
                                        lectureName = currLecture.lectureName,
                                        lectureDescription = currLecture.lectureDescription
                                    )
                                    lectureViewModel.deleteLecture(removedLecture)
                                    showConfirmationDialog("Lecture has been deleted")
                                }
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

    fun isValidUrl(url: String): Boolean
    {
        val urlPattern = Pattern.compile(
            "^(https?:\\/\\/)" +     // URL protocol
                    "(([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.([a-z]{2,}|imgur\\.com)|" + // Domain name or imgur.com
                    "((\\d{1,3}\\.){3}\\d{1,3}))" +   // OR IP (v4) address
                    "(:\\d{1,5})?" +                // Optional port
                    "(\\/[-a-z\\d%_.~+]*)*" +       // Path
                    "(\\?[;&a-z\\d%_.~+=-]*)?" +    // Query string
                    "(\\#[-a-z\\d_]*)?\$",          // Fragment locator
            Pattern.CASE_INSENSITIVE
        )

        val matcher = urlPattern.matcher(url)
        return matcher.matches()
    }

    private fun checkUserFields(firstField: EditText, secondField: EditText, thirdField: EditText): Boolean {

        if (firstField.text.isEmpty() || secondField.text.isEmpty() || thirdField.text.isEmpty()) {
            Toast.makeText(requireContext(), "Every field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }

        val email = firstField.text.toString().trim()
        if (!isValidEmail(email)) {
            Toast.makeText(requireContext(), "Bad e-mail format", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check for password length
        if (thirdField.text.toString().length < 8) {
            Toast.makeText(requireContext(), "Password must consist of 8 characters at least", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    private fun checkCourseFields(courseField : EditText): Boolean{

        if(courseField.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "This field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkUpdatedUserFields(currentEmail : EditText, newEmail : EditText,
                                       currentUsername : EditText, newUsername : EditText,
                                       newPassword : EditText): Boolean{
        if(currentEmail.text.toString().isEmpty()
            || newEmail.text.toString().isEmpty()
            || currentUsername.text.toString().isEmpty()
            || newUsername.text.toString().isEmpty()
            || newPassword.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "Every field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkUpdatedCourseFields(currentCourse : EditText, newCourse : EditText): Boolean{
        if(currentCourse.text.toString().isEmpty() || newCourse.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "Every field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkLectureFields(lectureName : TextInputLayout, lectureDesc : TextInputLayout): Boolean{
        if(lectureName.editText?.text.toString().isEmpty() || lectureDesc.editText?.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "Every field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkRemovedUserFields(removeUserField : EditText): Boolean{
        if(removeUserField.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "This field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkRemovedCourseFields(removeCourseField : EditText): Boolean{
        if(removeCourseField.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "This field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showConfirmationDialog(message : String) {
        val inflater = LayoutInflater.from(requireContext())
        val confirmationDialogView = inflater.inflate(R.layout.confirmation_dialog, null)
        val confirmationText = confirmationDialogView.findViewById<TextView>(R.id.confirmation_text)
        confirmationText.text = message

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(confirmationDialogView)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun logout() {
        preferenceManager.logout()
        val action = AdminFragmentDirections.actionAdminFragmentToWelcomeFragment()
        findNavController().navigate(action)
    }
}