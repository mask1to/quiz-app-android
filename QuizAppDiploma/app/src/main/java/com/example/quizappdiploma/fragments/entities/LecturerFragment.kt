package com.example.quizappdiploma.fragments.entities

import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
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
import com.example.quizappdiploma.database.quizzes.QuizDataRepository
import com.example.quizappdiploma.database.quizzes.QuizModel
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.database.users.UserDataRepository
import com.example.quizappdiploma.database.users.UserModel
import com.example.quizappdiploma.databinding.FragmentLecturerBinding
import com.example.quizappdiploma.fragments.lectures.LectureFragmentDirections
import com.example.quizappdiploma.fragments.lists.CourseListFragment
import com.example.quizappdiploma.fragments.viewmodels.*
import com.example.quizappdiploma.fragments.viewmodels.factory.*
import com.example.quizappdiploma.preferences.PreferenceManager
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class LecturerFragment : Fragment()
{
    private var _binding : FragmentLecturerBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager
    private val coursesList = CourseListFragment()
    private lateinit var courseViewModel : CourseViewModel
    private lateinit var lectureViewModel : LectureViewModel
    private lateinit var quizQuestionViewModel : QuizQuestionViewModel

    private var currentList: String = "user"
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        //checkLoginStatus()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        _binding = FragmentLecturerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val navState = navController.saveState()

                navController.popBackStack(R.id.courseFragment, true)

                requireActivity().moveTaskToBack(true)

                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.addButton.isEnabled = false
        binding.updateButton.isEnabled = false
        binding.removeButton.isEnabled = false

        val courseDao = MyDatabase.getDatabase(requireContext()).courseDao()
        val lectureDao = MyDatabase.getDatabase(requireContext()).lectureDao()
        val quizQuestionDao = MyDatabase.getDatabase(requireContext()).quizQuestionDao()

        val courseRepository = CourseDataRepository(courseDao)
        val lectureRepository = LectureDataRepository(lectureDao)
        val quizQuestionRepository = QuizQuestionDataRepository(quizQuestionDao)

        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(courseRepository))[CourseViewModel::class.java]
        lectureViewModel = ViewModelProvider(this, LectureViewModelFactory(lectureRepository))[LectureViewModel::class.java]
        quizQuestionViewModel = ViewModelProvider(this, QuizQuestionViewModelFactory(quizQuestionRepository))[QuizQuestionViewModel::class.java]

        val coursesRadioButton = binding.coursesRadioButton
        val lecturesRadioButton = binding.lecturesRadioButton
        val questionsRadioButton = binding.questionsRadioButton

        val radioButtonClickListener = View.OnClickListener { view ->
            coursesRadioButton.isChecked = view.id == R.id.coursesRadioButton
            lecturesRadioButton.isChecked = view.id == R.id.lecturesRadioButton
            questionsRadioButton.isChecked = view.id == R.id.questionsRadioButton

            when (view.id) {
                R.id.coursesRadioButton -> {
                    currentList = "course"
                    binding.addButton.isEnabled = true
                    binding.updateButton.isEnabled = true
                    binding.removeButton.isEnabled = true
                }
                R.id.lecturesRadioButton -> {
                    currentList = "lecture"
                    binding.addButton.isEnabled = true
                    binding.updateButton.isEnabled = true
                    binding.removeButton.isEnabled = true
                }
                R.id.questionsRadioButton -> {
                    currentList = "question"
                    binding.addButton.isEnabled = true
                    binding.updateButton.isEnabled = true
                    binding.removeButton.isEnabled = true
                }
            }
        }

        coursesRadioButton.setOnClickListener(radioButtonClickListener)
        lecturesRadioButton.setOnClickListener(radioButtonClickListener)
        questionsRadioButton.setOnClickListener(radioButtonClickListener)

        val loggedInUser = preferenceManager.getLoggedInUser()
        binding.nameTextView.text = loggedInUser.username
        binding.emailTextView.text = loggedInUser.email

        switchFragment(coursesList)


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
            "question" -> when(action)
            {
                "add" -> R.layout.custom_dialog_add_question
                "update" -> R.layout.custom_dialog_update_question
                "remove" -> R.layout.custom_dialog_remove_question
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
        val questionMenu = dialog.findViewById<TextInputLayout>(R.id.questionMenu)
        val imageUrlField = dialog.findViewById<TextInputLayout>(R.id.imageUrlField)
        val newLectureDesc = dialog.findViewById<TextInputLayout>(R.id.newLectureDescDialogField)
        val questionNameTextInputLayout = dialog.findViewById<TextInputLayout>(R.id.questionNameField)
        val imagePathTextInputLayout = dialog.findViewById<TextInputLayout>(R.id.questionImagePath)
        val questionPointsTextInputLayout = dialog.findViewById<TextInputLayout>(R.id.questionPointsField)
        val questionDifficultyTextInputLayout = dialog.findViewById<TextInputLayout>(R.id.questionDifficultyField)
        val questionFirstAnswer = dialog.findViewById<TextInputLayout>(R.id.firstAnswerField)
        val questionSecondAnswer = dialog.findViewById<TextInputLayout>(R.id.secondAnswerField)
        val questionThirdAnswer = dialog.findViewById<TextInputLayout>(R.id.thirdAnswerField)
        val questionFourthAnswer = dialog.findViewById<TextInputLayout>(R.id.fourthAnswerField)
        val questionCorrectAnswer = dialog.findViewById<TextInputLayout>(R.id.correctAnswerField)

        if (courseMenu != null) {
            val adapter = ArrayAdapter(requireContext(), R.layout.entity_dropdown_item, mutableListOf<String>())
            (courseMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            courseViewModel.getCourseNames().observeOnce(viewLifecycleOwner) { courses ->
                val courseNames = courses.map { it.courseName }
                val courseIds = courses.map { it.id }

                adapter.clear()
                adapter.addAll(courseNames)
                val courseAutoCompleteTextView = (courseMenu.editText as? AutoCompleteTextView)
                courseAutoCompleteTextView?.setAdapter(adapter)

                courseAutoCompleteTextView?.setOnItemClickListener { parent, _, position, _ ->
                    val selectedCourseId = courseIds[position]

                    if (selectedCourseId != null && lectureMenu != null) {
                        val lectureAdapter = ArrayAdapter(requireContext(), R.layout.entity_dropdown_item, mutableListOf<String>())
                        (lectureMenu.editText as? AutoCompleteTextView)?.setAdapter(lectureAdapter)

                        lectureViewModel.getLectureNameByCourseId(selectedCourseId).observeOnce(viewLifecycleOwner) { lectures ->
                            val lectureNames = lectures.mapNotNull { it.lectureName } // Filter out null values

                            lectureAdapter.clear()
                            lectureAdapter.addAll(lectureNames)
                            val lectureAutoCompleteTextView = (lectureMenu.editText as? AutoCompleteTextView)
                            lectureAutoCompleteTextView?.setAdapter(lectureAdapter)

                            lectureAutoCompleteTextView?.setOnItemClickListener { _, _, position, _ ->
                                val selectedLectureName = lectureAutoCompleteTextView.adapter.getItem(position).toString()

                                lectureViewModel.getLectureDescByLectureName(selectedLectureName).observeOnce(viewLifecycleOwner) { lecture ->
                                    if (lecture != null) {
                                        if (lecture.isNotEmpty()) {
                                            val currLecture = lecture.first()
                                            if (newLectureDesc != null) {
                                                newLectureDesc.editText?.setText(currLecture.lectureDescription)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (selectedCourseId != null && questionMenu != null) {
                        val questionAdapter = ArrayAdapter(requireContext(), R.layout.entity_dropdown_item, mutableListOf<String>())
                        (questionMenu.editText as? AutoCompleteTextView)?.setAdapter(questionAdapter)

                        // Replace this with your ViewModel and method to get question names by courseId
                        quizQuestionViewModel.getQuestionNamesByCourseId(selectedCourseId).observeOnce(viewLifecycleOwner) { questions ->
                            val questionNames = questions.mapNotNull { it.questionName } // Filter out null values

                            questionAdapter.clear()
                            questionAdapter.addAll(questionNames)
                            val questionAutoCompleteTextView = (questionMenu.editText as? AutoCompleteTextView)
                            questionAutoCompleteTextView?.setAdapter(questionAdapter)

                            // Add any specific actions when a question is selected, if needed
                            questionAutoCompleteTextView?.setOnItemClickListener { _, _, position, _ ->
                                val selectedQuestionName = questionAutoCompleteTextView.adapter.getItem(position).toString()
                                quizQuestionViewModel.getQuestionPropsByQuestionName(selectedQuestionName).observeOnce(viewLifecycleOwner){questionProps->
                                    if(questionProps != null)
                                    {
                                        if(questionProps.isNotEmpty())
                                        {
                                            val currProps = questionProps.first()


                                            if(questionNameTextInputLayout != null || imagePathTextInputLayout != null || questionPointsTextInputLayout != null || questionDifficultyTextInputLayout != null
                                                || questionFirstAnswer != null || questionSecondAnswer != null || questionThirdAnswer != null || questionFourthAnswer != null || questionCorrectAnswer != null)
                                            {
                                                questionNameTextInputLayout.editText?.setText(currProps.questionName)
                                                imagePathTextInputLayout.editText?.setText(currProps.image_path)
                                                questionPointsTextInputLayout.editText?.setText(currProps.questionPoints.toString())
                                                questionDifficultyTextInputLayout.editText?.setText(currProps.questionDifficulty.toString())
                                                questionFirstAnswer.editText?.setText(currProps.questionOptionA)
                                                questionSecondAnswer.editText?.setText(currProps.questionOptionB)
                                                questionThirdAnswer.editText?.setText(currProps.questionOptionC)
                                                questionFourthAnswer.editText?.setText(currProps.questionOptionD)
                                                questionCorrectAnswer.editText?.setText(currProps.answer.toString())
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
                    if (listType == "course")
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
                    else if (listType == "lecture")
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
                    else if(listType == "question")
                    {
                        val courseName = courseMenu.editText?.text.toString()

                        courseViewModel.getCourseIdByName(courseName).observeOnce(viewLifecycleOwner){course ->
                            if(course != null)
                            {
                                val url = imagePathTextInputLayout?.editText?.text.toString()
                                if(isValidUrl(url))
                                {
                                    if(course.isNotEmpty())
                                    {
                                        val cId = course.first()
                                        val questionPoints = questionPointsTextInputLayout?.editText?.text.toString().toIntOrNull()
                                        val questionDifficulty = questionDifficultyTextInputLayout?.editText?.text.toString().toIntOrNull()
                                        val correctAnswer = questionCorrectAnswer?.editText?.text.toString().toIntOrNull()
                                        if(questionPoints != null && questionDifficulty != null && correctAnswer != null)
                                        {
                                            if(checkQuestionFields(questionNameTextInputLayout, imagePathTextInputLayout, questionPointsTextInputLayout, questionDifficultyTextInputLayout,
                                                    questionFirstAnswer, questionSecondAnswer, questionThirdAnswer, questionFourthAnswer, questionCorrectAnswer))
                                            {
                                                val newQuestion = QuizQuestionModel(
                                                    id = null,
                                                    courseId = cId.id,
                                                    questionName = questionNameTextInputLayout?.editText?.text.toString(),
                                                    image_path = url,
                                                    questionPoints = questionPoints,
                                                    questionDifficulty = questionDifficulty,
                                                    questionOptionA = questionFirstAnswer?.editText?.text.toString(),
                                                    questionOptionB = questionSecondAnswer?.editText?.text.toString(),
                                                    questionOptionC = questionThirdAnswer?.editText?.text.toString(),
                                                    questionOptionD = questionFourthAnswer?.editText?.text.toString(),
                                                    answer = correctAnswer,
                                                    alreadyUsed = 0
                                                )
                                                quizQuestionViewModel.addQuestion(newQuestion)
                                                showConfirmationDialog("Question has been added")
                                            }
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

                    if (listType == "course")
                    {
                        val currentCourseName = courseMenu.editText?.text.toString()
                        val newCourseName = dialog.findViewById<EditText>(R.id.newCourseNameField)

                        courseViewModel.getCourseByName(currentCourseName).observeOnce(viewLifecycleOwner){ course ->
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
                    else if (listType == "lecture")
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
                    else if(listType == "question")
                    {
                        val currentQuestionName = questionMenu.editText?.text.toString()

                        quizQuestionViewModel.getQuestionPropsByQuestionName(currentQuestionName).observeOnce(viewLifecycleOwner){currProps ->
                            if(currProps != null)
                            {
                                if(currProps.isNotEmpty())
                                {
                                    val url = imagePathTextInputLayout.editText?.text.toString()
                                    if(isValidUrl(url))
                                    {
                                        val questionPoints = questionPointsTextInputLayout.editText?.text.toString().toIntOrNull()
                                        val questionDifficulty = questionDifficultyTextInputLayout.editText?.text.toString().toIntOrNull()
                                        val correctAnswer = questionCorrectAnswer.editText?.text.toString().toIntOrNull()
                                        if(questionPoints != null && questionDifficulty != null && correctAnswer != null)
                                        {
                                            val currQuestion = currProps.first()
                                            val updatedQuestion = QuizQuestionModel(
                                                id = currQuestion.id,
                                                courseId = currQuestion.courseId,
                                                questionName = questionNameTextInputLayout.editText?.text.toString(),
                                                image_path = imagePathTextInputLayout.editText?.text.toString(),
                                                questionPoints = questionPoints,
                                                questionDifficulty = questionDifficulty,
                                                questionOptionA = questionFirstAnswer.editText?.text.toString(),
                                                questionOptionB = questionSecondAnswer.editText?.text.toString(),
                                                questionOptionC = questionThirdAnswer.editText?.text.toString(),
                                                questionOptionD = questionFourthAnswer.editText?.text.toString(),
                                                answer = correctAnswer,
                                                alreadyUsed = 0
                                            )
                                            quizQuestionViewModel.updateQuestion(updatedQuestion)
                                            showConfirmationDialog("Question has been updated")
                                        }
                                        else
                                        {
                                            Toast.makeText(requireContext(), "Fill in all fields", Toast.LENGTH_SHORT).show()
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
                }
                "remove" ->
                {
                    if (listType == "course")
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
                    else if (listType == "lecture")
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
                    else if(listType == "question")
                    {
                        val currentQuestionName = questionMenu.editText?.text.toString()

                        quizQuestionViewModel.getQuestionPropsByQuestionName(currentQuestionName).observeOnce(viewLifecycleOwner){currProps ->
                            if(currProps != null)
                            {
                                if(currProps.isNotEmpty())
                                {
                                    val currQuestionProps = currProps.first()
                                    val removedQuestion = QuizQuestionModel(
                                        id = currQuestionProps.id,
                                        courseId = currQuestionProps.courseId,
                                        questionName = currQuestionProps.questionName,
                                        image_path = currQuestionProps.image_path,
                                        questionPoints = currQuestionProps.questionPoints,
                                        questionDifficulty = currQuestionProps.questionDifficulty,
                                        questionOptionA = currQuestionProps.questionOptionA,
                                        questionOptionB = currQuestionProps.questionOptionB,
                                        questionOptionC = currQuestionProps.questionOptionC,
                                        questionOptionD = currQuestionProps.questionOptionD,
                                        answer = currQuestionProps.answer,
                                        alreadyUsed = currQuestionProps.alreadyUsed
                                    )
                                    quizQuestionViewModel.deleteQuestion(removedQuestion)
                                    showConfirmationDialog("Question has been deleted")
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


    private fun isValidUrl(url: String): Boolean
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

    private fun checkCourseFields(courseField : EditText): Boolean{

        if(courseField.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "This field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkQuestionFields(
        questionName : TextInputLayout,
        imagePath : TextInputLayout,
        questionPoints : TextInputLayout,
        questionDifficulty : TextInputLayout,
        questionOptionA : TextInputLayout,
        questionOptionB : TextInputLayout,
        questionOptionC : TextInputLayout,
        questionOptionD : TextInputLayout,
        correctAnswer : TextInputLayout
    ) : Boolean
    {
        if(questionName.editText?.text.toString().isEmpty() || imagePath.editText?.text.toString().isEmpty() || questionPoints.editText?.text.toString().isEmpty()
            || questionDifficulty.editText?.text.toString().isEmpty() || questionOptionA.editText?.text.toString().isEmpty() || questionOptionB.editText?.text.toString().isEmpty()
            || questionOptionC.editText?.text.toString().isEmpty() || questionOptionD.editText?.text.toString().isEmpty() || correctAnswer.editText?.text.toString().isEmpty())
        {
            Toast.makeText(requireContext(), "Every field is mandatory", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkUpdatedCourseFields(currentCourse : String, newCourse : EditText): Boolean{
        if(currentCourse.isEmpty() || newCourse.text.toString().isEmpty())
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
        val action = LecturerFragmentDirections.actionLecturerFragmentToWelcomeFragment()
        findNavController().navigate(action)
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}