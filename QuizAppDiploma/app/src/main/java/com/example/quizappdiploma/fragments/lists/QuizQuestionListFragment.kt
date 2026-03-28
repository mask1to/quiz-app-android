package com.example.quizappdiploma.fragments.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.lists.QuizQuestionListAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.databinding.FragmentQuestionListBinding
import com.example.quizappdiploma.fragments.viewmodels.CourseViewModel
import com.example.quizappdiploma.fragments.viewmodels.QuizQuestionViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.CourseViewModelFactory
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizQuestionViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class QuizQuestionListFragment : Fragment() {

    private var _binding: FragmentQuestionListBinding? = null
    private val binding get() = _binding!!
    private lateinit var questionViewModel: QuizQuestionViewModel
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var adapter: QuizQuestionListAdapter
    private var courses: List<CourseModel> = emptyList()
    private var selectedCourseId: Int? = null
    private var currentQuestionsLiveData: LiveData<List<QuizQuestionModel>>? = null
    private val questionsObserver = Observer<List<QuizQuestionModel>> { questions ->
        adapter.questionData = questions
        binding.emptyState.visibility = if (questions.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuestionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = MyDatabase.getDatabase(requireContext())
        questionViewModel = ViewModelProvider(this, QuizQuestionViewModelFactory(QuizQuestionDataRepository(db.quizQuestionDao())))[QuizQuestionViewModel::class.java]
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(CourseDataRepository(db.courseDao())))[CourseViewModel::class.java]

        adapter = QuizQuestionListAdapter(
            onEdit = { question -> showQuestionDialog(question) },
            onDelete = { question -> showDeleteConfirm(question) }
        )
        binding.questionList.layoutManager = LinearLayoutManager(requireContext())
        binding.questionList.adapter = adapter

        val dropdown = binding.courseFilterDropdown as AutoCompleteTextView

        courseViewModel.getCoursesByIdAsc().observe(viewLifecycleOwner) { courseList ->
            courses = courseList
            adapter.courseNameMap = courseList.associate { it.id!! to (it.courseName ?: "") }

            val courseNames = mutableListOf("All courses")
            courseNames.addAll(courseList.map { it.courseName ?: "Course #${it.id}" })
            val dropdownAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, courseNames)
            dropdown.setAdapter(dropdownAdapter)

            // If no course selected yet, auto-select first course if available
            if (selectedCourseId == null && courseList.isNotEmpty()) {
                selectedCourseId = courseList.first().id
                dropdown.setText(courseList.first().courseName ?: "Course #${courseList.first().id}", false)
                loadQuestionsForCourse(selectedCourseId)
            }
        }

        dropdown.setOnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                selectedCourseId = null
                loadQuestionsForCourse(null)
            } else {
                val course = courses.getOrNull(position - 1)
                selectedCourseId = course?.id
                loadQuestionsForCourse(selectedCourseId)
            }
        }

        binding.fab.setOnClickListener { showQuestionDialog(null) }
    }

    private fun loadQuestionsForCourse(courseId: Int?) {
        currentQuestionsLiveData?.removeObserver(questionsObserver)
        val newLiveData = if (courseId == null) {
            questionViewModel.getAllQuestionsLive()
        } else {
            questionViewModel.getQuestionNamesByCourseId(courseId)
        }
        currentQuestionsLiveData = newLiveData
        newLiveData.observe(viewLifecycleOwner, questionsObserver)
    }

    private fun showQuestionDialog(existing: QuizQuestionModel?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_question, null)
        val questionLayout = dialogView.findViewById<TextInputLayout>(R.id.questionNameLayout)
        val optionALayout = dialogView.findViewById<TextInputLayout>(R.id.optionALayout)
        val optionBLayout = dialogView.findViewById<TextInputLayout>(R.id.optionBLayout)
        val optionCLayout = dialogView.findViewById<TextInputLayout>(R.id.optionCLayout)
        val optionDLayout = dialogView.findViewById<TextInputLayout>(R.id.optionDLayout)
        val correctLayout = dialogView.findViewById<TextInputLayout>(R.id.correctAnswerLayout)
        val diffLayout = dialogView.findViewById<TextInputLayout>(R.id.difficultyLayout)

        if (existing != null) {
            questionLayout.editText?.setText(existing.questionName)
            optionALayout.editText?.setText(existing.questionOptionA)
            optionBLayout.editText?.setText(existing.questionOptionB)
            optionCLayout.editText?.setText(existing.questionOptionC)
            optionDLayout.editText?.setText(existing.questionOptionD)
            correctLayout.editText?.setText(existing.answer?.toString())
            diffLayout.editText?.setText(existing.questionDifficulty?.toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (existing == null) "Add Question" else "Edit Question")
            .setView(dialogView)
            .setPositiveButton(if (existing == null) "Add" else "Update") { _, _ ->
                val questionText = questionLayout.editText?.text.toString().trim()
                val optA = optionALayout.editText?.text.toString().trim()
                val optB = optionBLayout.editText?.text.toString().trim()
                val optC = optionCLayout.editText?.text.toString().trim()
                val optD = optionDLayout.editText?.text.toString().trim()
                val correct = correctLayout.editText?.text.toString().toIntOrNull()
                val diff = diffLayout.editText?.text.toString().toIntOrNull() ?: 1

                if (questionText.isEmpty() || optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (correct == null || correct !in 1..4) {
                    Toast.makeText(requireContext(), "Correct answer must be 1–4", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val courseId = existing?.courseId ?: selectedCourseId ?: courses.firstOrNull()?.id
                val question = QuizQuestionModel(
                    id = existing?.id,
                    courseId = courseId,
                    questionName = questionText,
                    image_path = existing?.image_path,
                    questionPoints = diff,
                    questionDifficulty = diff,
                    questionOptionA = optA,
                    questionOptionB = optB,
                    questionOptionC = optC,
                    questionOptionD = optD,
                    answer = correct,
                    alreadyUsed = existing?.alreadyUsed ?: 0
                )
                if (existing == null) {
                    questionViewModel.addQuestion(question)
                } else {
                    questionViewModel.updateWholeQuestion(question)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirm(question: QuizQuestionModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Question")
            .setMessage("Delete this question?\n\n\"${question.questionName?.take(80)}\"")
            .setPositiveButton("Delete") { _, _ -> questionViewModel.deleteQuestion(question) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
