package com.example.quizappdiploma.fragments.quizzes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentResultQuizBinding


class ResultQuizFragment : Fragment()
{
    private var _binding : FragmentResultQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var usernameTextView : TextView
    private lateinit var resultTextView : TextView
    private lateinit var scoreTextView : TextView
    private lateinit var congratsTextView: TextView
    private lateinit var trophyAnimation : LottieAnimationView
    private lateinit var btnFinish : Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentResultQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        usernameTextView = binding.textViewUserName
        resultTextView = binding.textViewResult
        scoreTextView = binding.textViewScore
        congratsTextView = binding.textViewCongrats
        trophyAnimation = binding.trophyAnimation
        btnFinish = binding.btnFinish
        setupAnimation(trophyAnimation)
    }

    @SuppressLint("Range")
    private fun setupAnimation(animationView: LottieAnimationView)
    {
        animationView.speed = 3.0F // How fast does the animation play
        animationView.progress = 50F // Starts the animation from 50% of the beginning
        animationView.setAnimation(R.raw.trophy)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }
}