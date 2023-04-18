package com.example.quizappdiploma.fragments.content

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.databinding.FragmentContentBinding
import com.example.quizappdiploma.fragments.viewmodels.LectureViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.LectureViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.io.FileInputStream

class ContentFragment : Fragment()
{
    private var _binding : FragmentContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var lectureTitle : TextView
    private lateinit var lectureDescription : TextView
    private lateinit var lectureImage : ImageView
    private lateinit var nextLectureButton: FloatingActionButton
    private lateinit var previousLectureButton : FloatingActionButton
    private lateinit var startQuizButton: FloatingActionButton
    private lateinit var lectureViewModel: LectureViewModel
    private lateinit var picasso: Picasso

    private var currentLectureIndex = 0
    private val args : ContentFragmentArgs by navArgs()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences("LectureCounterPrefs", Context.MODE_PRIVATE)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private val internetPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.INTERNET, false) -> {
                Log.d("granted internet", "granted")

            }
            else -> {
                Log.d("not granted internet", "not granted")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        parentFragment?.activity?.actionBar?.hide()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                // Save the current navigation state
                val navController = findNavController()
                val navState = navController.saveState()

                // Remove all the previous fragments from the back stack
                navController.popBackStack(R.id.quizFragment, true)

                // Minimize the app
                requireActivity().moveTaskToBack(true)

                // Restore the navigation state when the app is resumed
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        lectureTitle = binding.lectureTitleTxt
        lectureDescription = binding.lectureDescriptionTxt
        lectureImage = binding.lectureImageView
        nextLectureButton = binding.lectureNextBtn
        previousLectureButton = binding.lecturePreviousBtn
        startQuizButton = binding.lectureStartQuizBtn

        lectureTitle.text = args.lectureTitle
        lectureDescription.text = args.lectureDescription

        val dao = MyDatabase.getDatabase(requireContext()).lectureDao()
        val repository = LectureDataRepository(dao)
        lectureViewModel = ViewModelProvider(this, LectureViewModelFactory(repository))[LectureViewModel::class.java]


        val cacheSize = 20 * 1024 * 1024 // 20 MB
        val cache = Cache(requireContext().cacheDir, cacheSize.toLong())
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .build()

        picasso = Picasso.Builder(requireContext())
            .downloader(OkHttp3Downloader(okHttpClient))
            .indicatorsEnabled(false)
            .loggingEnabled(true)
            .build()

        var imageUrl = args.imagePath

        Picasso.get()
            .load(imageUrl)
            .noFade()
            .into(lectureImage, object : Callback {
                override fun onSuccess() {
                    binding.imageProgressBar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    binding.imageProgressBar.visibility = View.GONE
                    Log.e("Err: ", "Error loading image: ${e?.message}")
                }
            })


        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            contentmodel = lectureViewModel
        }

        val myArgs = arguments
        val courseId = myArgs?.getInt("course_id")
        var lectureId = myArgs?.getInt("lecture_id")
        val imagePath = arguments?.getString("imagePath") ?: ""
        val lectureTitle = arguments?.getString("lecture_title") ?: ""
        val lectureDescription = arguments?.getString("lecture_description") ?: ""
        Log.d("courseId in content: ", courseId.toString())
        Log.d("lectureId in content: ", lectureId.toString())


        //todo: lecture_id preniest z content do content
        if (lectureId != null) {
            lectureViewModel.getLecturesByCourseId(courseId!!).observe(viewLifecycleOwner) { lectures ->
                val lectureIndex = lectures.indexOfFirst { it.id == lectureId }
                if (lectureIndex != -1) {
                    currentLectureIndex = lectureIndex
                    val visitedLectureIds = sharedPreferences.getIntegerSet("visitedLectures_${courseId}", emptySet()).toMutableSet()
                    visitedLectureIds.add(lectureId)
                    sharedPreferences.edit().putIntegerSet("visitedLectures_${courseId}", visitedLectureIds).apply()
                }

                if (currentLectureIndex + 1 == lectures.size) {
                    val visitedLectureIds = sharedPreferences.getIntegerSet("visitedLectures_${courseId}", emptySet())
                    val allLecturesVisited = visitedLectureIds.size == lectures.size
                    startQuizButton.isEnabled = allLecturesVisited
                    startQuizButton.visibility = View.VISIBLE
                    nextLectureButton.isEnabled = false
                    previousLectureButton.isEnabled = true
                } else {
                    startQuizButton.visibility = View.GONE
                    nextLectureButton.isEnabled = true
                    previousLectureButton.isEnabled = true
                }

                Log.d("lectures size: ", lectures.size.toString())

                nextLectureButton.setOnClickListener {
                    if (currentLectureIndex + 1 < lectures.size) {
                        currentLectureIndex++
                        val nextLecture = lectures[currentLectureIndex]
                        imageUrl = lectures[currentLectureIndex].image_path.toString()
                        val action = ContentFragmentDirections.actionContentFragmentSelf(nextLecture.id!!, nextLecture.lectureName!!, nextLecture.lectureDescription!!, imageUrl, courseId!!)
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }

                previousLectureButton.setOnClickListener {
                    if (currentLectureIndex - 1 >= 0) {
                        currentLectureIndex--
                        val previousLecture = lectures[currentLectureIndex]
                        imageUrl = lectures[currentLectureIndex].image_path.toString()
                        val action = ContentFragmentDirections.actionContentFragmentSelf(previousLecture.id!!, previousLecture.lectureName!!, previousLecture.lectureDescription!!, imageUrl, courseId!!)
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }

                startQuizButton.setOnClickListener {
                    val action = ContentFragmentDirections.actionContentFragmentToQuizFragment(courseId!!)
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        }

    }

    fun SharedPreferences.Editor.putIntegerSet(key: String, values: Set<Int>): SharedPreferences.Editor {
        return putStringSet(key, values.map { it.toString() }.toSet())
    }

    fun SharedPreferences.getIntegerSet(key: String, defValues: Set<Int>): Set<Int> {
        return getStringSet(key, defValues.map { it.toString() }.toSet())?.map { it.toInt() }?.toSet() ?: defValues
    }

}