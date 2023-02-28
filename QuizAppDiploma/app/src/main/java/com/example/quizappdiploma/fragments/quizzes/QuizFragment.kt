package com.example.quizappdiploma.fragments.quizzes

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.quizzes.questions.ConstantsBeta
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.databinding.FragmentAdminBinding
import com.example.quizappdiploma.databinding.FragmentQuizBinding

class QuizFragment : Fragment(), OnClickListener
{

    private var _binding : FragmentQuizBinding? = null
    private val binding get() = _binding!!

    //getUsername of user, who is doing it
    private val username = "masko"

    private var correctAnswers : Int = 0

    private lateinit var progressBar : ProgressBar
    private lateinit var submitBtn : Button
    private lateinit var textViewProgress : TextView
    private lateinit var textViewQuestion : TextView
    private lateinit var imageQuestion : ImageView
    private lateinit var textViewFirstOption : TextView
    private lateinit var textViewSecondOption : TextView
    private lateinit var textViewThirdOption : TextView
    private lateinit var textViewFourthOption : TextView

    private var myCurrentPosition : Int = 1
    private var myQuestionList : ArrayList<QuizQuestionModel>? = null
    private var mySelectedOption : Int = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        submitBtn = binding.btnSubmit
        textViewProgress = binding.textViewProgress
        textViewQuestion = binding.textViewQuestion
        imageQuestion = binding.imageQuestion
        textViewFirstOption = binding.textViewFirstOption
        textViewSecondOption = binding.textViewSecondOption
        textViewThirdOption = binding.textViewThirdOption
        textViewFourthOption = binding.textViewFourthOption

        textViewFirstOption.setOnClickListener(this)
        textViewSecondOption.setOnClickListener(this)
        textViewThirdOption.setOnClickListener(this)
        textViewFourthOption.setOnClickListener(this)
        submitBtn.setOnClickListener(this)

        myQuestionList = ConstantsBeta.getFirstFiveQuestions()
        setQuestion()
        //defaultOptionsView()

    }

    private fun setQuestion()
    {
        defaultOptionsView()
        //myCurrentPosition = 1
        val question: QuizQuestionModel = myQuestionList!![myCurrentPosition - 1]

        progressBar.progress = myCurrentPosition
        textViewProgress.text = "$myCurrentPosition/${progressBar.max}"
        question.image?.let { imageQuestion.setImageResource(it) }
        textViewQuestion.text = question.questionName
        textViewFirstOption.text = question.questionOptionA
        textViewSecondOption.text = question.questionOptionB
        textViewThirdOption.text = question.questionOptionC
        textViewFourthOption.text = question.questionOptionD

        if(myCurrentPosition == myQuestionList!!.size)
        {
            submitBtn.text = "Finish"
        }
        else
        {
            submitBtn.text = "Next"
        }
    }

    private fun defaultOptionsView()
    {
        val questionOptions = ArrayList<TextView>()
        textViewFirstOption.let {
            questionOptions.add(0, it)
        }

        textViewSecondOption.let {
            questionOptions.add(1, it)
        }

        textViewThirdOption.let {
            questionOptions.add(2, it)
        }

        textViewFourthOption.let {
            questionOptions.add(3, it)
        }

        for(option in questionOptions)
        {
            option.setTextColor((Color.parseColor("#FF0000")))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.default_option_border_bg
            )
        }

    }

    private fun selectedOptionView(txtView : TextView, selectedOption : Int)
    {
        defaultOptionsView()

        mySelectedOption = selectedOption
        txtView.setTextColor(Color.parseColor("#363A43"))
        txtView.setTypeface(txtView.typeface, Typeface.BOLD)
        txtView.background = ContextCompat.getDrawable(
            requireContext(), R.drawable.default_option_border_bg
        )
    }

    private fun answerView(answer : Int, drawableView : Int)
    {
        when(answer){
            1 -> {
                textViewFirstOption.background = ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }

            2 -> {
                textViewFirstOption.background = ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }

            3 -> {
                textViewFirstOption.background = ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }

            4 -> {
                textViewFirstOption.background = ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }
        }
    }

    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
            R.id.textViewFirstOption -> {
                textViewFirstOption.let {
                    selectedOptionView(it, 1)
                }
            }

            R.id.textViewSecondOption -> {
                textViewSecondOption.let {
                    selectedOptionView(it, 2)
                }
            }

            R.id.textViewThirdOption -> {
                textViewThirdOption.let {
                    selectedOptionView(it, 3)
                }
            }

            R.id.textViewFourthOption -> {
                textViewFourthOption.let {
                    selectedOptionView(it, 4)
                }
            }

            R.id.btnSubmit -> {
                if(mySelectedOption == 0)
                {
                    myCurrentPosition++

                    when{
                        myCurrentPosition <= myQuestionList!!.size ->{
                            setQuestion()
                        }
                        else ->{
                            //Toast.makeText(requireContext(), "You made it", Toast.LENGTH_SHORT).show()
                            //TODO: send correct_answers, username, total_questions
                            val action = QuizFragmentDirections.actionQuizFragmentToResultQuizFragment(username, myQuestionList!!.size, correctAnswers)
                            Navigation.findNavController(requireView()).navigate(action)
                        }
                    }
                }
                else
                {
                    val question = myQuestionList?.get(myCurrentPosition - 1)
                    if(question!!.answer != mySelectedOption)
                    {
                        answerView(mySelectedOption, R.drawable.wrong_option_border_bg)
                    }
                    else
                    {
                        answerView(mySelectedOption, R.drawable.correct_option_border_bg)
                        correctAnswers++
                    }

                    if(myCurrentPosition == myQuestionList!!.size)
                    {
                        submitBtn.text = "Finish"
                    }
                    else
                    {
                        submitBtn.text = "Next"
                    }

                    mySelectedOption = 0
                }
            }
        }
    }
}