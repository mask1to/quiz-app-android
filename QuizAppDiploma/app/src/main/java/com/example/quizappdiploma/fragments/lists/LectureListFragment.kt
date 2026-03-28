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
import com.example.quizappdiploma.adapters.lists.LectureListAdapter
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseDataRepository
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.databinding.FragmentLectureListBinding
import com.example.quizappdiploma.fragments.viewmodels.CourseViewModel
import com.example.quizappdiploma.fragments.viewmodels.LectureViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.CourseViewModelFactory
import com.example.quizappdiploma.fragments.viewmodels.factory.LectureViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class LectureListFragment : Fragment() {

    private var _binding: FragmentLectureListBinding? = null
    private val binding get() = _binding!!
    private lateinit var lectureViewModel: LectureViewModel
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var adapter: LectureListAdapter
    private var courses: List<CourseModel> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLectureListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = MyDatabase.getDatabase(requireContext())
        lectureViewModel = ViewModelProvider(this, LectureViewModelFactory(LectureDataRepository(db.lectureDao())))[LectureViewModel::class.java]
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(CourseDataRepository(db.courseDao())))[CourseViewModel::class.java]

        adapter = LectureListAdapter(
            onEdit = { lecture -> showLectureDialog(lecture) },
            onDelete = { lecture -> showDeleteConfirm(lecture) }
        )
        binding.lectureList.layoutManager = LinearLayoutManager(requireContext())
        binding.lectureList.adapter = adapter

        courseViewModel.getCoursesByIdAsc().observe(viewLifecycleOwner) { courseList ->
            courses = courseList
            adapter.courseNameMap = courseList.associate { it.id!! to (it.courseName ?: "") }
        }

        lectureViewModel.getAllLectures().observe(viewLifecycleOwner) { lectures ->
            adapter.lectureData = lectures
            binding.emptyState.visibility = if (lectures.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fab.setOnClickListener { showLectureDialog(null) }
    }

    private fun showLectureDialog(existing: LectureModel?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_lecture, null)
        val nameLayout = dialogView.findViewById<TextInputLayout>(R.id.lectureNameLayout)
        val descLayout = dialogView.findViewById<TextInputLayout>(R.id.lectureDescLayout)
        val imageLayout = dialogView.findViewById<TextInputLayout>(R.id.imageUrlLayout)

        if (existing != null) {
            nameLayout.editText?.setText(existing.lectureName)
            descLayout.editText?.setText(existing.lectureDescription)
            imageLayout.editText?.setText(existing.image_path)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (existing == null) "Add Lecture" else "Edit Lecture")
            .setView(dialogView)
            .setPositiveButton(if (existing == null) "Add" else "Update") { _, _ ->
                val name = nameLayout.editText?.text.toString().trim()
                val desc = descLayout.editText?.text.toString().trim()
                val imageUrl = imageLayout.editText?.text.toString().trim().ifEmpty { null }

                if (name.isEmpty()) {
                    Toast.makeText(requireContext(), "Lecture name is required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (existing == null) {
                    val courseId = courses.firstOrNull()?.id
                    lectureViewModel.addLecture(LectureModel(null, courseId, imageUrl, name, desc))
                } else {
                    lectureViewModel.updateLecture(LectureModel(existing.id, existing.course_id, imageUrl ?: existing.image_path, name, desc))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirm(lecture: LectureModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Lecture")
            .setMessage("Delete \"${lecture.lectureName}\"?")
            .setPositiveButton("Delete") { _, _ -> lectureViewModel.deleteLecture(lecture) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
