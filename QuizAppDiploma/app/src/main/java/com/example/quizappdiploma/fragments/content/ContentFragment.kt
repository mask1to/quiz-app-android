package com.example.quizappdiploma.fragments.content

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.lectures.LectureDataRepository
import com.example.quizappdiploma.database.lectures.LectureModel
import com.example.quizappdiploma.databinding.FragmentContentBinding
import com.example.quizappdiploma.fragments.viewmodels.ContentViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.LectureViewModelFactory
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ContentFragment : Fragment()
{
    private var _binding : FragmentContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var lectureTitle : TextView
    private lateinit var lectureDescription : TextView
    private lateinit var lectureImage : ImageView
    private lateinit var nextLectureButton: Button
    private lateinit var contentViewModel: ContentViewModel
    private var myPathList : ArrayList<LectureModel>? = null

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

        lectureTitle.text = args.lectureTitle
        lectureDescription.text = args.lectureDescription

        val dao = MyDatabase.getDatabase(requireContext()).lectureDao()
        val repository = LectureDataRepository(dao)
        contentViewModel = ViewModelProvider(this, LectureViewModelFactory(repository))[ContentViewModel::class.java]

        contentViewModel.getLectureImagePaths().observe(viewLifecycleOwner) { paths ->

            if (myPathList == null) {
                myPathList = ArrayList()
            }

            myPathList?.addAll(paths)

            val context = requireContext()
            for (imageUrl in paths) {
                val fileName = "cached_image_${imageUrl.hashCode()}.jpg"
                val cachedBitmap = getCachedImage(context, fileName)
                if (cachedBitmap == null) {
                    downloadAndCacheImage(context, imageUrl.image_path.toString())
                }
            }
        }

        val imageUrl = args.imagePath
        val context = requireContext()

        val cachedImage = getCachedImage(context, "cached_image_${imageUrl.hashCode()}.jpg")

        if (cachedImage != null)
        {
            lectureImage.setImageBitmap(cachedImage)
        }
        else
        {
            //lectureImage.setImageResource(R.drawable.cow)
            downloadAndCacheImage(context, imageUrl)
            Picasso.get().load(imageUrl).into(lectureImage)
        }


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
                val action = ContentFragmentDirections.actionContentFragmentToQuizFragment(courseId!!)
                //val action = ContentFragmentDirections.actionContentFragmentSelf(lectureId, lectureTitle, lectureDescription, imagePath, courseId!!)
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
    }

    private fun downloadAndCacheImage(context: Context, imageUrl: String)
    {
        Picasso.get().load(imageUrl).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                val cacheDir = context.cacheDir
                val fileName = "cached_image_${imageUrl.hashCode()}.jpg"
                val file = File(cacheDir, fileName)
                val outputStream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
    }


    private fun getCachedImage(context: Context, fileName: String): Bitmap?
    {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)

        if (!file.exists())
        {
            return null
        }

        val inputStream = FileInputStream(file)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        return bitmap
    }






}