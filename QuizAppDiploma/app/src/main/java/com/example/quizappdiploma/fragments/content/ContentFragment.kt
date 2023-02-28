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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.databinding.FragmentContentBinding
import com.example.quizappdiploma.fragments.viewmodels.ContentViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.ContentViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
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
        contentViewModel = ViewModelProvider(this, ContentViewModelFactory(repository))[ContentViewModel::class.java]

        lectureTitle.text = args.lectureTitle
        lectureDescription.text = args.lectureDescription

        //todo: make loading while fetching the image
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

        nextLectureButton.setOnClickListener {
            Log.d("test", "click")
            val action = ContentFragmentDirections.actionContentFragmentToQuizFragment()
            Navigation.findNavController(requireView()).navigate(action)
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

    private fun checkPermissions(): Boolean
    {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED
    }

}