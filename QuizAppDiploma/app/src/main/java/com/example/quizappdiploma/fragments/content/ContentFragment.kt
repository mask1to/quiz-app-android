package com.example.quizappdiploma.fragments.content

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.databinding.FragmentContentBinding
import com.example.quizappdiploma.fragments.viewmodels.ContentViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.LectureViewModelFactory
import java.net.HttpURLConnection
import java.net.URL

class ContentFragment : Fragment()
{
    private var _binding : FragmentContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var lectureTitle : TextView
    private lateinit var lectureDescription : TextView
    private lateinit var lectureImage : ImageView
    private lateinit var nextLectureButton: Button
    private lateinit var contentViewModel: ContentViewModel

    private val args : ContentFragmentArgs by navArgs()

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

        lectureTitle = binding.lectureTitleTxt
        lectureDescription = binding.lectureDescriptionTxt
        lectureImage = binding.lectureImageView
        nextLectureButton = binding.lectureNextBtn

        val dao = MyDatabase.getDatabase(requireContext()).lectureDao()
        val repository = LectureDataRepository(dao)
        contentViewModel = ViewModelProvider(this, LectureViewModelFactory(repository))[ContentViewModel::class.java]

        lectureTitle.text = args.lectureTitle
        lectureDescription.text = args.lectureDescription

        //todo: make loading while fetching the image#
        // cache images, picasso alebo glide
        val imageUrl = args.imagePath
        Thread{
            val bitmap = downloadImage(imageUrl)

            activity?.runOnUiThread {
                lectureImage.setImageBitmap(bitmap)
            }
        }.start()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            contentmodel = contentViewModel
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
            nextLectureButton.setOnClickListener {
                //val action = ContentFragmentDirections.actionContentFragmentToQuizFragment(courseId!!)
                val action = ContentFragmentDirections.actionContentFragmentSelf(lectureId, lectureTitle, lectureDescription, imagePath, courseId!!)
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
    }

    private fun downloadImage(url: String): Bitmap? {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("TAG", "Error downloading image: ${e.message}")
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showPermissionAlertDialog()
    {
        val alertDialog: AlertDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Internet access needed")
                setPositiveButton("OK"
                ) { dialog, id ->
                    internetPermissionRequest.launch(
                        arrayOf(
                            Manifest.permission.INTERNET
                        )
                    )
                }
                setNegativeButton("Cancel"
                ) { dialog, id ->

                }
            }
            builder.create()
        }
        alertDialog.show()
    }
}