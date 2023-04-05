package com.example.quizappdiploma.fragments.quizzes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
    private lateinit var finalAnimation : LottieAnimationView
    private lateinit var btnFinish : Button
    private var backPressedCallback: OnBackPressedCallback? = null
    private val args : ResultQuizFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,
            backPressedCallback as OnBackPressedCallback
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentResultQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        usernameTextView = binding.textViewUserName
        resultTextView = binding.textViewResult
        scoreTextView = binding.textViewScore
        congratsTextView = binding.textViewCongrats
        finalAnimation = binding.finalAnimation
        btnFinish = binding.btnFinish

        usernameTextView.text = args.username
        val correctAnswers = args.correctAnswers
        val totalQuestions = args.totalQuestions

        scoreTextView.text = "Your score is $correctAnswers out of $totalQuestions"
        setupAnimation(finalAnimation, correctAnswers)

        btnFinish.setOnClickListener{
            val action = ResultQuizFragmentDirections.actionResultQuizFragmentToStudentFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    @SuppressLint("Range")
    private fun setupAnimation(animationView: LottieAnimationView, correctAnswers : Int)
    {
        animationView.speed = 1.0F // How fast does the animation play
        animationView.progress = 35F // Starts the animation from 50% of the beginning
        when (correctAnswers)
        {
            in 0..2 -> {
                animationView.setAnimation(R.raw.noknowledge)
            }
            in 3..5 -> {
                animationView.setAnimation(R.raw.neutral)
            }
            in 6..8 -> {
                animationView.setAnimation(R.raw.great)
            }
            in 9..10 -> {
                animationView.setAnimation(R.raw.trophy)
            }
        }
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }
}