package com.example.quizappdiploma.fragments.quizzes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizappdiploma.databinding.FragmentResultQuizBinding

class ResultQuizFragment : Fragment() {

    private var _binding: FragmentResultQuizBinding? = null
    private val binding get() = _binding!!
    private val args: ResultQuizFragmentArgs by navArgs()
    private var backPressedCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { /* block back during result */ }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResultQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val correctAnswers = args.correctAnswers
        val totalQuestions = args.totalQuestions
        val wrongAnswers = totalQuestions - correctAnswers
        val percent = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0

        binding.textViewUserName.text = args.username
        binding.textViewScorePercent.text = "$percent%"
        binding.textViewScore.text = "You answered $correctAnswers out of $totalQuestions correctly"
        binding.textViewCorrectCount.text = correctAnswers.toString()
        binding.textViewWrongCount.text = wrongAnswers.toString()
        binding.textViewTotalCount.text = totalQuestions.toString()
        binding.scoreCircle.progress = percent

        binding.textViewCongrats.text = when {
            percent >= 90 -> "Outstanding!"
            percent >= 70 -> "Well Done!"
            percent >= 50 -> "Good Effort!"
            else -> "Keep Practicing!"
        }

        binding.textViewResult.text = when {
            percent >= 90 -> "You nailed it. Perfect score territory!"
            percent >= 70 -> "You have a solid understanding."
            percent >= 50 -> "You're getting there. Review and retry!"
            else -> "Don't give up. Learning takes time!"
        }

        binding.btnFinish.setOnClickListener {
            val action = ResultQuizFragmentDirections.actionResultQuizFragmentToStudentFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
