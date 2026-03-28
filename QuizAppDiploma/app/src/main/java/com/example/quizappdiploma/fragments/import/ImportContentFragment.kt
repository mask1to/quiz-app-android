package com.example.quizappdiploma.fragments.import

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.courses.CourseModel
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.database.quizzes.QuizModel
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.databinding.FragmentImportContentBinding
import com.example.quizappdiploma.network.RetrofitClient
import com.example.quizappdiploma.network.models.OpenTdbCategory
import kotlinx.coroutines.launch

class ImportContentFragment : Fragment() {

    private var _binding: FragmentImportContentBinding? = null
    private val binding get() = _binding!!

    private var categories: List<OpenTdbCategory> = emptyList()
    private var selectedCategory: OpenTdbCategory? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentImportContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SeekBar label update (min=5, so progress+5)
        binding.questionsSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.questionsCountTxt.text = (progress + 1).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.questionsCountTxt.text = (binding.questionsSeekBar.progress + 1).toString()

        // Load OpenTDB categories
        loadCategories()

        binding.importBtn.setOnClickListener { startImport() }
    }

    private fun loadCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.openTdb.getCategories()
                categories = response.categories
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    categories.map { it.name }
                )
                binding.categoryDropdown.setAdapter(adapter)
                binding.categoryDropdown.setOnItemClickListener { _, _, position, _ ->
                    selectedCategory = categories[position]
                    // Auto-fill course name if empty
                    if (binding.courseNameInput.text.isNullOrEmpty()) {
                        binding.courseNameInput.setText(categories[position].name)
                    }
                    // Auto-fill Wikipedia topic if empty
                    if (binding.wikipediaTopicInput.text.isNullOrEmpty()) {
                        binding.wikipediaTopicInput.setText(categories[position].name)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Could not load categories. Check your internet connection.", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startImport() {
        val courseName = binding.courseNameInput.text?.toString()?.trim()
        val wikiTopic = binding.wikipediaTopicInput.text?.toString()?.trim()
        val questionCount = binding.questionsSeekBar.progress + 1
        val category = selectedCategory

        if (courseName.isNullOrEmpty()) {
            binding.courseNameInput.error = "Enter a course name"
            return
        }
        if (category == null) {
            Toast.makeText(requireContext(), "Please select a quiz category", Toast.LENGTH_SHORT).show()
            return
        }

        val difficulty = when (binding.difficultyChipGroup.checkedChipId) {
            R.id.chipEasy -> "easy"
            R.id.chipMedium -> "medium"
            R.id.chipHard -> "hard"
            else -> null // mixed = no filter
        }

        setImporting(true, "Creating course...")

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val db = MyDatabase.getDatabase(requireContext())

                // 1. Insert course, get ID
                val courseId = db.courseDao().addCourseReturnId(
                    CourseModel(id = null, courseName = courseName)
                ).toInt()

                // 2. Fetch Wikipedia and insert lecture (if topic provided)
                if (!wikiTopic.isNullOrEmpty()) {
                    setImporting(true, "Fetching lecture from Wikipedia...")
                    val titleEncoded = wikiTopic.replace(" ", "_")
                    try {
                        // Full article text (up to 8000 chars)
                        val actionResponse = RetrofitClient.wikipedia.getFullArticle(titles = titleEncoded)
                        val page = actionResponse.query?.pages?.values?.firstOrNull()
                        val fullText = page?.extract?.trim()

                        // Thumbnail from summary endpoint
                        val imageUrl = try {
                            RetrofitClient.wikipedia.getPageSummary(titleEncoded).thumbnail?.source
                        } catch (_: Exception) { null }

                        if (!fullText.isNullOrEmpty()) {
                            db.lectureDao().addLecture(
                                LectureModel(
                                    id = null,
                                    course_id = courseId,
                                    image_path = imageUrl,
                                    lectureName = page?.title ?: courseName,
                                    lectureDescription = fullText
                                )
                            )
                        } else {
                            throw Exception("Empty article")
                        }
                    } catch (e: Exception) {
                        // Wikipedia failed — insert placeholder lecture
                        db.lectureDao().addLecture(
                            LectureModel(
                                id = null,
                                course_id = courseId,
                                image_path = null,
                                lectureName = courseName,
                                lectureDescription = "Lecture content for $courseName."
                            )
                        )
                    }
                }

                // 3. Insert quiz linked to course
                setImporting(true, "Creating quiz...")
                val quizId = db.quizDao().insertQuizReturnId(
                    QuizModel(
                        id = null,
                        course_id = courseId,
                        quizName = "$courseName Quiz",
                        quizTotalPoints = questionCount
                    )
                ).toInt()

                // 4. Fetch OpenTDB questions
                setImporting(true, "Fetching $questionCount questions from OpenTDB...")
                val questionsResponse = RetrofitClient.openTdb.getQuestions(
                    amount = questionCount,
                    category = category.id,
                    difficulty = difficulty
                )

                if (questionsResponse.responseCode != 0 || questionsResponse.results.isEmpty()) {
                    setImporting(false)
                    Toast.makeText(
                        requireContext(),
                        "No questions found for this category/difficulty combination. Try different settings.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                // 5. Map and insert each question
                setImporting(true, "Saving ${questionsResponse.results.size} questions...")
                questionsResponse.results.forEach { q ->
                    val correctDecoded = decodeHtml(q.correctAnswer)
                    val allOptions = (q.incorrectAnswers.map { decodeHtml(it) } + correctDecoded).shuffled()
                    val correctIndex = allOptions.indexOf(correctDecoded) + 1 // 1-indexed

                    val difficultyInt = when (q.difficulty) {
                        "easy" -> 1
                        "medium" -> 2
                        "hard" -> 3
                        else -> 1
                    }
                    val points = difficultyInt

                    db.quizQuestionDao().addQuestion(
                        QuizQuestionModel(
                            id = null,
                            courseId = courseId,
                            questionName = decodeHtml(q.question),
                            image_path = null,
                            questionPoints = points,
                            questionDifficulty = difficultyInt,
                            questionOptionA = allOptions.getOrNull(0) ?: "",
                            questionOptionB = allOptions.getOrNull(1) ?: "",
                            questionOptionC = allOptions.getOrNull(2) ?: "",
                            questionOptionD = allOptions.getOrNull(3) ?: "",
                            answer = correctIndex,
                            alreadyUsed = 0
                        )
                    )
                }

                setImporting(false)
                Toast.makeText(
                    requireContext(),
                    "✓ Imported \"$courseName\" with ${questionsResponse.results.size} questions!",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigateUp()

            } catch (e: Exception) {
                setImporting(false)
                Toast.makeText(requireContext(), "Import failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun decodeHtml(text: String): String =
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()

    @SuppressLint("SetTextI18n")
    private fun setImporting(importing: Boolean, status: String = "") {
        binding.importBtn.isEnabled = !importing
        binding.statusCard.visibility = if (importing) View.VISIBLE else View.GONE
        binding.importStatusTxt.text = status
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
