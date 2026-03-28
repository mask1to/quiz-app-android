package com.example.quizappdiploma.fragments.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappdiploma.R
import com.example.quizappdiploma.adapters.lists.CourseListAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.databinding.FragmentCourseListBinding
import com.example.quizappdiploma.fragments.viewmodels.CourseViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.CourseViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class CourseListFragment : Fragment() {

    private var _binding: FragmentCourseListBinding? = null
    private val binding get() = _binding!!
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var adapter: CourseListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = MyDatabase.getDatabase(requireContext())
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(CourseDataRepository(db.courseDao())))[CourseViewModel::class.java]

        adapter = CourseListAdapter(
            onEdit = { course -> showCourseDialog(course) },
            onDelete = { course -> showDeleteConfirm(course) }
        )
        binding.courseList.layoutManager = LinearLayoutManager(requireContext())
        binding.courseList.adapter = adapter

        courseViewModel.getCoursesByIdAsc().observe(viewLifecycleOwner) { courses ->
            adapter.courseData = courses
            binding.emptyState.visibility = if (courses.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fab.setOnClickListener { showCourseDialog(null) }
    }

    private fun showCourseDialog(existing: CourseModel?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_course, null)
        val nameLayout = dialogView.findViewById<TextInputLayout>(R.id.courseNameLayout)
        if (existing != null) nameLayout.editText?.setText(existing.courseName)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (existing == null) "Add Course" else "Edit Course")
            .setView(dialogView)
            .setPositiveButton(if (existing == null) "Add" else "Update") { _, _ ->
                val name = nameLayout.editText?.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(requireContext(), "Course name is required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (existing == null) {
                    courseViewModel.addCourse(CourseModel(null, name))
                } else {
                    courseViewModel.updateCourse(CourseModel(existing.id, name))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirm(course: CourseModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Course")
            .setMessage("Delete \"${course.courseName}\"?\n\nAll lectures, questions and quizzes in this course will also be permanently deleted.")
            .setPositiveButton("Delete All") { _, _ ->
                val courseId = course.id ?: return@setPositiveButton
                viewLifecycleOwner.lifecycleScope.launch {
                    val db = MyDatabase.getDatabase(requireContext())
                    // Delete in order: deepest FK dependencies first
                    db.userAnswersDao().deleteAnswersByCourseId(courseId)
                    db.quizQuestionDao().deleteQuestionsByCourseId(courseId)
                    db.quizDao().deleteQuizzesByCourseId(courseId)
                    db.lectureDao().deleteLecturesByCourseId(courseId)
                    db.courseDao().deleteCourse(course)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
