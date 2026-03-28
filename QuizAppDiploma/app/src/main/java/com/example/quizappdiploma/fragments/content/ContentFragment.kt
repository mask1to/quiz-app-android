package com.example.quizappdiploma.fragments.content

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.databinding.FragmentContentBinding
import com.example.quizappdiploma.fragments.viewmodels.LectureViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.LectureViewModelFactory
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.OkHttpClient

class ContentFragment : Fragment() {

    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var nextLectureButton: MaterialButton
    private lateinit var previousLectureButton: MaterialButton
    private lateinit var startQuizButton: MaterialButton
    private lateinit var lectureViewModel: LectureViewModel
    private lateinit var picasso: Picasso
    private lateinit var sharedPreferences: SharedPreferences

    private var currentLectureIndex = 0
    private val REQUEST_CODE_PERMISSIONS = 1000
    private val args: ContentFragmentArgs by navArgs()

    // Track how many paragraph cards have been "passed" while scrolling
    private var paragraphCount = 0
    private var paragraphsRead = 0
    private var hasReachedBottom = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences("LectureCounterPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val navState = navController.saveState()
                navController.popBackStack(R.id.quizFragment, true)
                requireActivity().moveTaskToBack(true)
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        nextLectureButton = binding.lectureNextBtn
        previousLectureButton = binding.lecturePreviousBtn
        startQuizButton = binding.lectureStartQuizBtn

        binding.lectureTitleTxt.text = args.lectureTitle

        // Build paragraph cards from description
        buildParagraphCards(args.lectureDescription)

        // Set reading time estimate (~200 words per minute)
        val wordCount = args.lectureDescription.split("\\s+".toRegex()).size
        val minutes = maxOf(1, wordCount / 200)
        binding.readingTimeText.text = "~$minutes min read"

        val dao = MyDatabase.getDatabase(requireContext()).lectureDao()
        val repository = LectureDataRepository(dao)
        lectureViewModel = ViewModelProvider(this, LectureViewModelFactory(repository))[LectureViewModel::class.java]

        setupPicasso()
        loadImage(args.imagePath)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            contentmodel = lectureViewModel
        }

        // Scroll listener — updates reading progress bar
        binding.scrollViewContent.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val scrollView = binding.scrollViewContent
            val totalScrollRange = scrollView.getChildAt(0).height - scrollView.height
            if (totalScrollRange > 0) {
                val progress = ((scrollY.toFloat() / totalScrollRange) * 100).toInt().coerceIn(0, 100)
                binding.readingProgress.progress = progress
                binding.sectionsProgress.progress = progress
                binding.sectionsReadTxt.text = "$progress% read"

                // Unlock quiz when user has read 80%+ on the last lecture
                if (progress >= 80 && !hasReachedBottom) {
                    hasReachedBottom = true
                    checkAndShowQuizButton()
                }
            }
        }

        val courseId = arguments?.getInt("course_id")
        val lectureId = arguments?.getInt("lecture_id")

        if (lectureId != null && courseId != null) {
            lectureViewModel.getLecturesByCourseId(courseId).observe(viewLifecycleOwner) { lectures ->
                val lectureIndex = lectures.indexOfFirst { it.id == lectureId }
                if (lectureIndex != -1) {
                    currentLectureIndex = lectureIndex
                    val visited = sharedPreferences.getIntegerSet("visitedLectures_$courseId", emptySet()).toMutableSet()
                    visited.add(lectureId)
                    sharedPreferences.edit().putIntegerSet("visitedLectures_$courseId", visited).apply()
                }

                val isLastLecture = currentLectureIndex + 1 == lectures.size

                if (isLastLecture) {
                    nextLectureButton.isEnabled = false
                    previousLectureButton.isEnabled = currentLectureIndex > 0
                    // Quiz button shown only after scrolling 80%
                    checkAndShowQuizButton()
                } else {
                    startQuizButton.visibility = View.GONE
                    nextLectureButton.isEnabled = true
                    previousLectureButton.isEnabled = currentLectureIndex > 0
                }

                nextLectureButton.setOnClickListener {
                    if (currentLectureIndex + 1 < lectures.size) {
                        currentLectureIndex++
                        val next = lectures[currentLectureIndex]
                        val action = ContentFragmentDirections.actionContentFragmentSelf(
                            next.id!!, next.lectureName!!, next.lectureDescription!!, next.image_path.toString(), courseId
                        )
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }

                previousLectureButton.setOnClickListener {
                    if (currentLectureIndex - 1 >= 0) {
                        currentLectureIndex--
                        val prev = lectures[currentLectureIndex]
                        val action = ContentFragmentDirections.actionContentFragmentSelf(
                            prev.id!!, prev.lectureName!!, prev.lectureDescription!!, prev.image_path.toString(), courseId
                        )
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }

                startQuizButton.setOnClickListener {
                    val action = ContentFragmentDirections.actionContentFragmentToQuizFragment(courseId)
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        }
    }

    private fun checkAndShowQuizButton() {
        val courseId = arguments?.getInt("course_id") ?: return
        val lectureId = arguments?.getInt("lecture_id") ?: return
        lectureViewModel.getLecturesByCourseId(courseId).observe(viewLifecycleOwner) { lectures ->
            val isLastLecture = currentLectureIndex + 1 == lectures.size
            if (isLastLecture) {
                val visited = sharedPreferences.getIntegerSet("visitedLectures_$courseId", emptySet())
                val allVisited = visited.size == lectures.size
                if (hasReachedBottom && allVisited) {
                    startQuizButton.visibility = View.VISIBLE
                    startQuizButton.isEnabled = true
                } else if (hasReachedBottom) {
                    // Has read this one, still show it enabled (quiz unlocked by reading)
                    startQuizButton.visibility = View.VISIBLE
                    startQuizButton.isEnabled = true
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun buildParagraphCards(description: String) {
        val container = binding.contentContainer
        container.removeAllViews()

        // Split on double newline first, fall back to single newline
        val rawParagraphs = description
            .split("\n\n", "\r\n\r\n")
            .flatMap { block ->
                // If a block has internal single newlines, keep them as one paragraph
                listOf(block.trim())
            }
            .filter { it.isNotBlank() }

        // If no natural breaks found, split into chunks of ~3 sentences
        val paragraphs = if (rawParagraphs.size == 1) {
            splitIntoSentenceGroups(description, groupSize = 3)
        } else {
            rawParagraphs
        }

        paragraphCount = paragraphs.size

        val accentColors = listOf(
            R.color.md_primary,
            R.color.md_secondary,
            R.color.md_tertiary,
            R.color.app_accent_green,
            R.color.md_primary,
            R.color.md_secondary
        )

        paragraphs.forEachIndexed { index, text ->
            val cardView = LayoutInflater.from(requireContext())
                .inflate(R.layout.content_paragraph_card, container, false)

            cardView.findViewById<TextView>(R.id.sectionNumber).text = "§ ${index + 1}"
            cardView.findViewById<TextView>(R.id.paragraphText).text = text

            // Cycle through accent colors for the left bar
            val color = ContextCompat.getColor(requireContext(), accentColors[index % accentColors.size])
            cardView.findViewById<View>(R.id.accentBar).setBackgroundColor(color)
            cardView.findViewById<TextView>(R.id.sectionNumber).backgroundTintList =
                android.content.res.ColorStateList.valueOf(color)

            container.addView(cardView)
        }
    }

    private fun splitIntoSentenceGroups(text: String, groupSize: Int): List<String> {
        val sentences = text.split(Regex("(?<=[.!?])\\s+")).filter { it.isNotBlank() }
        return sentences.chunked(groupSize) { chunk -> chunk.joinToString(" ") }
            .ifEmpty { listOf(text) }
    }

    private fun setupPicasso() {
        if (!hasPermissions()) {
            requestPermissions()
            return
        }
        val cacheSize = 20 * 1024 * 1024
        val cache = Cache(requireContext().cacheDir, cacheSize.toLong())
        val okHttpClient = OkHttpClient.Builder().cache(cache).build()
        picasso = Picasso.Builder(requireContext())
            .downloader(OkHttp3Downloader(okHttpClient))
            .indicatorsEnabled(false)
            .loggingEnabled(true)
            .build()
    }

    private fun loadImage(imageUrl: String?) {
        if (imageUrl.isNullOrEmpty()) {
            binding.imageProgressBar.visibility = View.GONE
            return
        }
        binding.imageProgressBar.visibility = View.VISIBLE
        Picasso.get()
            .load(imageUrl)
            .noFade()
            .into(binding.lectureImageView, object : Callback {
                override fun onSuccess() {
                    binding.imageProgressBar.visibility = View.GONE
                }
                override fun onError(e: Exception?) {
                    binding.imageProgressBar.visibility = View.GONE
                    Log.e("ContentFragment", "Error loading image: ${e?.message}")
                }
            })
    }

    private fun hasPermissions(): Boolean {
        val ctx = requireContext()
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isEmpty() || !grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(requireContext(), "Internet permission is required to load images.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun SharedPreferences.Editor.putIntegerSet(key: String, values: Set<Int>): SharedPreferences.Editor =
        putStringSet(key, values.map { it.toString() }.toSet())

    private fun SharedPreferences.getIntegerSet(key: String, defValues: Set<Int>): Set<Int> =
        getStringSet(key, defValues.map { it.toString() }.toSet())?.map { it.toInt() }?.toSet() ?: defValues

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
