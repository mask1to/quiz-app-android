package com.example.quizappdiploma.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentProfileBinding
import com.example.quizappdiploma.databinding.FragmentStudentBinding

class ProfileFragment : Fragment()
{

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileAnimation : LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileAnimation = binding.cowAnimation
        setupAnimation(profileAnimation)


        binding.courseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_studentFragment_to_courseFragment)
        }
    }

    @SuppressLint("Range")
    private fun setupAnimation(animationView: LottieAnimationView)
    {
        animationView.speed = 1.0F // How fast does the animation play
        animationView.progress = 35F // Starts the animation from 50% of the beginning
        animationView.setAnimation(R.raw.fitnesscow)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }
}