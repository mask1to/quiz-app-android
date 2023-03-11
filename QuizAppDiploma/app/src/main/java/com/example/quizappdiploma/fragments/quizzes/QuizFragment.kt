package com.example.quizappdiploma.fragments.quizzes

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.databinding.FragmentQuizBinding
import com.example.quizappdiploma.fragments.viewmodels.QuizQuestionViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizQuestionViewModelFactory

class QuizFragment : Fragment(), OnClickListener
{

    private var _binding : FragmentQuizBinding? = null
    private val binding get() = _binding!!

    //getUsername of user, who is doing it
    private val username = "masko"
    private lateinit var quizQuestionViewModel: QuizQuestionViewModel
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
    private var myQuestionList2 : ArrayList<QuizQuestionModel>? = null
    private var mySelectedOption : Int = 0
    private var correctAnswers : Int = 0


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

        val dao = MyDatabase.getDatabase(requireContext()).quizQuestionDao()
        val repository = QuizQuestionDataRepository(dao)
        quizQuestionViewModel = ViewModelProvider(this, QuizQuestionViewModelFactory(repository))[QuizQuestionViewModel::class.java]

        val myArgs = arguments
        val courseId = myArgs?.getInt("course_id")

        quizQuestionViewModel.getFirstFiveQuestions(courseId!!, 10).observe(viewLifecycleOwner) { firstQuestions ->

            val questionList = firstQuestions

            if (myQuestionList == null) {
                myQuestionList = ArrayList()
            }

            // Add all questions from questionList to myQuestionList
            myQuestionList?.addAll(questionList)
            Log.d("myQuestionList", myQuestionList.toString())

            setQuestion()
        }

        binding.btnSubmit.setOnClickListener {

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
                Log.d("myCurrentPosition: ", myCurrentPosition.toString())

                val question = myQuestionList?.get(myCurrentPosition - 1)
                Log.d("Question answer:", question!!.answer.toString())
                Log.d("Selected option:", mySelectedOption.toString())
                if(question!!.answer != mySelectedOption)
                {
                    answerView(mySelectedOption, R.drawable.wrong_option_border_bg)
                }
                else
                {
                    answerView(mySelectedOption, R.drawable.correct_option_border_bg)
                    correctAnswers++
                }

                if(myCurrentPosition == 10)
                {
                    submitBtn.text = "Finish"
                }
                else
                {
                    submitBtn.text = "Next"
                }

                mySelectedOption = 0
                Log.d("correctAnswers: ", correctAnswers.toString())

                if(myCurrentPosition == 5 && correctAnswers == 5)
                {
                    //TODO: generate questions with diff 1 2 3 3 3
                    Log.d("5 spravnych", "spustam 1 2 3 3 3")
                    //generateQuestions(quizQuestionViewModel, courseId, 0,1,1,3)

                }
                else if(myCurrentPosition == 5 && correctAnswers == 4)
                {
                    //TODO: generate questions with diff 1 2 2 3 3
                    Log.d("4 spravnych", "spustam 1 2 2 3 3")
                }
                else if(myCurrentPosition == 5 && correctAnswers == 3)
                {
                    //TODO: generate questions with diff 1 2 2 2 3
                    Log.d("3 spravnych", "spustam 1 2 2 2 3")
                }
                else if(myCurrentPosition == 5 && correctAnswers == 2)
                {
                    //TODO: generate questions with diff 1 1 2 2 2
                    Log.d("2 spravnych", "spustam 1 1 2 2 2")
                }
                else if(myCurrentPosition == 5 && correctAnswers == 1)
                {
                    //TODO: generate questions with diff 1 1 1 2 2
                    Log.d("1 spravnych", "spustam 1 1 1 2 2")
                }
                else if(myCurrentPosition == 5 && correctAnswers == 0)
                {
                    //TODO: generate questions with diff 1 1 1 1 2
                    Log.d("0 spravnych", "spustam 1 1 1 1 2")
                }
            }

        }
    }

    private fun setQuestion()
    {
        defaultOptionsView()
        //myCurrentPosition = 1
        val question: QuizQuestionModel = myQuestionList!![myCurrentPosition - 1]

        progressBar.progress = myCurrentPosition
        textViewProgress.text = "$myCurrentPosition/${progressBar.max}"
        Log.d("textViewProgress: ", "$myCurrentPosition/${progressBar.max}")
        //TODO: check how to set image from ContentFragment
        //question.image?.let { imageQuestion.setImageResource(question.image_path) }
        textViewQuestion.text = question.questionName
        textViewFirstOption.text = question.questionOptionA
        textViewSecondOption.text = question.questionOptionB
        textViewThirdOption.text = question.questionOptionC
        textViewFourthOption.text = question.questionOptionD

        //if(myCurrentPosition == myQuestionList!!.size)
        if(myCurrentPosition == 10)
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
                textViewSecondOption.background = ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }

            3 -> {
                textViewThirdOption.background = ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }

            4 -> {
                textViewFourthOption.background = ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }
        }
    }

    private fun generateQuestions(quizQuestionViewModel: QuizQuestionViewModel, courseId : Int, earlyQuestionLimit : Int, firstQuestionLimit : Int, secondQuestionLimit : Int, thirdQuestionLimit : Int)
    {
        quizQuestionViewModel.getFirstFiveQuestions(courseId!!, earlyQuestionLimit).observe(viewLifecycleOwner) { firstQuestions ->
            quizQuestionViewModel.getLastFiveQuestions(courseId, 1, firstQuestionLimit).observe(viewLifecycleOwner) { easyQuestions ->
                quizQuestionViewModel.getLastFiveQuestions(courseId, 2, secondQuestionLimit).observe(viewLifecycleOwner) { midQuestions ->
                    quizQuestionViewModel.getLastFiveQuestions(courseId, 3, thirdQuestionLimit).observe(viewLifecycleOwner) { hardQuestions ->
                        val questionList = firstQuestions + easyQuestions + midQuestions + hardQuestions

                        // Add all questions from questionList to myQuestionList
                        if(myQuestionList == null)
                        {
                            myQuestionList = ArrayList()
                        }
                        myQuestionList?.addAll(questionList)
                        Log.d("newQuestionList", myQuestionList.toString())

                        setQuestion()
                    }
                }
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
        }
    }
}