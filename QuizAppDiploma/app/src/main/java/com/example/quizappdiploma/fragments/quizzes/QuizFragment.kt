package com.example.quizappdiploma.fragments.quizzes

import android.annotation.SuppressLint
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
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
import com.example.quizappdiploma.database.quizzes.QuizDataRepository
import com.example.quizappdiploma.database.quizzes.answers.UserAnswers
import com.example.quizappdiploma.database.quizzes.answers.UserAnswersDataRepository
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionDataRepository
import com.example.quizappdiploma.database.quizzes.questions.QuizQuestionModel
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsDataRepository
import com.example.quizappdiploma.database.quizzes.stats.QuizStatsModel
import com.example.quizappdiploma.databinding.FragmentQuizBinding
import com.example.quizappdiploma.fragments.viewmodels.QuizQuestionViewModel
import com.example.quizappdiploma.fragments.viewmodels.QuizStatsViewModel
import com.example.quizappdiploma.fragments.viewmodels.QuizViewModel
import com.example.quizappdiploma.fragments.viewmodels.UserAnswersViewModel
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizQuestionViewModelFactory
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizStatsViewModelFactory
import com.example.quizappdiploma.fragments.viewmodels.factory.QuizViewModelFactory
import com.example.quizappdiploma.fragments.viewmodels.factory.UserAnswersViewModelFactory
import com.example.quizappdiploma.preferences.PreferenceManager
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.io.FileInputStream

class QuizFragment : Fragment(), OnClickListener
{

    private var _binding : FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var quizQuestionViewModel: QuizQuestionViewModel
    private lateinit var userAnswersViewModel: UserAnswersViewModel
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var quizStatsViewModel: QuizStatsViewModel

    private lateinit var progressBar : ProgressBar
    private lateinit var submitBtn : Button
    private lateinit var textViewProgress : TextView
    private lateinit var textViewQuestion : TextView
    private lateinit var imageQuestion : ImageView
    private lateinit var textViewFirstOption : TextView
    private lateinit var textViewSecondOption : TextView
    private lateinit var textViewThirdOption : TextView
    private lateinit var textViewFourthOption : TextView
    private lateinit var questionW : TextView
    private lateinit var picasso: Picasso
    private lateinit var preferenceManager: PreferenceManager

    private var additionalQuestionsGenerated = false
    private var questionStartTime: Double = 0.0
    private var myCurrentPosition : Int = 1
    private var myQuestionList : ArrayList<QuizQuestionModel>? = null
    private var mySelectedOption : Int = 0
    private var correctAnswers : Int = 0
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val navState = navController.saveState()
                navController.popBackStack(R.id.resultQuizFragment, true)
                requireActivity().moveTaskToBack(true)
                navController.restoreState(navState)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        progressBar = binding.progressBar
        submitBtn = binding.btnSubmit
        textViewProgress = binding.textViewProgress
        textViewQuestion = binding.textViewQuestion
        imageQuestion = binding.imageQuestion
        textViewFirstOption = binding.textViewFirstOption
        textViewSecondOption = binding.textViewSecondOption
        textViewThirdOption = binding.textViewThirdOption
        textViewFourthOption = binding.textViewFourthOption
        questionW = binding.textView4

        textViewFirstOption.setOnClickListener(this)
        textViewSecondOption.setOnClickListener(this)
        textViewThirdOption.setOnClickListener(this)
        textViewFourthOption.setOnClickListener(this)
        submitBtn.setOnClickListener(this)
        val loggedInUser = preferenceManager.getLoggedInUser()

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

        val dao = MyDatabase.getDatabase(requireContext()).quizQuestionDao()
        val answerDao = MyDatabase.getDatabase(requireContext()).userAnswersDao()
        val quizDao = MyDatabase.getDatabase(requireContext()).quizDao()
        val quizStatsDao = MyDatabase.getDatabase(requireContext()).quizStatsDao()

        val repository = QuizQuestionDataRepository(dao)
        val answerRepository = UserAnswersDataRepository(answerDao)
        val quizRepository = QuizDataRepository(quizDao)
        val quizStatsRepository = QuizStatsDataRepository(quizStatsDao)

        quizViewModel = ViewModelProvider(this, QuizViewModelFactory(quizRepository))[QuizViewModel::class.java]
        quizQuestionViewModel = ViewModelProvider(this, QuizQuestionViewModelFactory(repository))[QuizQuestionViewModel::class.java]
        userAnswersViewModel = ViewModelProvider(this, UserAnswersViewModelFactory(answerRepository))[UserAnswersViewModel::class.java]
        quizStatsViewModel = ViewModelProvider(this, QuizStatsViewModelFactory(quizStatsRepository))[QuizStatsViewModel::class.java]

        val myArgs = arguments
        val courseId = myArgs?.getInt("course_id")

        quizQuestionViewModel.getFirstFiveQuestions(courseId!!, 5).observeOnce(viewLifecycleOwner) { firstQuestions ->

            val questionList = firstQuestions

            if (myQuestionList == null) {
                myQuestionList = ArrayList()
            }

            myQuestionList?.addAll(questionList)
            binding.btnSubmit.isEnabled = false
            setQuestion()
        }

        binding.btnSubmit.setOnClickListener {

            val questionEndTime = System.currentTimeMillis()
            val timeSpent = questionEndTime - questionStartTime
            val timeSpentSeconds = timeSpent / 1000
            questionStartTime = questionEndTime.toDouble()

            if(mySelectedOption == 0)
            {
                binding.btnSubmit.isEnabled = false
                val previousQuestion = myQuestionList!![myCurrentPosition - 1]
                quizQuestionViewModel.updateQuestion(previousQuestion)

                myCurrentPosition++

                when{
                    myCurrentPosition <= myQuestionList!!.size ->{
                        setQuestion()
                    }
                    else ->{
                        quizViewModel.getAllQuizPropertiesByCourseId(courseId).observeOnce(viewLifecycleOwner){quizIds ->
                            if(quizIds != null)
                            {
                                if(quizIds.isNotEmpty())
                                {
                                    val currVals = quizIds.first()
                                    quizQuestionViewModel.resetAllQuestions()
                                    val loggedUser = preferenceManager.getLoggedInUser()
                                    val userQuizStats = QuizStatsModel(
                                        id = null,
                                        user_id = loggedUser!!.id,
                                        quiz_id = currVals.id,
                                        correctAnswers = correctAnswers,
                                        quizName = currVals.quizName
                                    )
                                    quizStatsViewModel.insertStats(userQuizStats)
                                    val action = QuizFragmentDirections.actionQuizFragmentToResultQuizFragment(
                                        loggedUser.username.toString(), myQuestionList!!.size, correctAnswers)
                                    Navigation.findNavController(requireView()).navigate(action)
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                val question = myQuestionList?.get(myCurrentPosition - 1)

                quizViewModel.getQuizIdByCourseId(courseId) { quizIds ->
                    val userAnswer = UserAnswers(
                        id = null,
                        user_id = loggedInUser!!.id,
                        question_id = question!!.id,
                        quiz_id = quizIds.first(),
                        answer = mySelectedOption,
                        time_spent = timeSpentSeconds,
                    )

                    userAnswersViewModel.addUserAnswer(userAnswer)
                }

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

                if (myCurrentPosition == 5 && !additionalQuestionsGenerated)
                {
                    additionalQuestionsGenerated = true
                    viewLifecycleOwner.lifecycleScope.launch {
                        val averageTime = quizQuestionViewModel.getAverageTimeSpentOnUsedQuestions()
                        updateQuizQuestions(correctAnswers, courseId, averageTime!!)
                    }
                }
            }
        }
    }

    private fun setQuestion()
    {
        defaultOptionsView()
        val question: QuizQuestionModel = myQuestionList!![myCurrentPosition - 1]

        progressBar.progress = myCurrentPosition
        textViewProgress.text = "$myCurrentPosition/${progressBar.max}"

        val imagePath = question.image_path

        Picasso.get()
            .load(imagePath)
            .noFade()
            .into(imageQuestion, object : Callback {
                override fun onSuccess() {
                    binding.imageProgressBar2.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    binding.imageProgressBar2.visibility = View.GONE
                    Log.e("Err: ", "Error loading image: ${e?.message}")
                }
            })

        textViewQuestion.text = question.questionName
        textViewFirstOption.text = question.questionOptionA
        textViewSecondOption.text = question.questionOptionB
        textViewThirdOption.text = question.questionOptionC
        textViewFourthOption.text = question.questionOptionD
        questionW.text = "Question weight: "+question.questionDifficulty.toString()

        //binding.btnSubmit.isEnabled = true
        binding.btnSubmit.text = "Submit"
        questionStartTime = System.currentTimeMillis().toDouble()
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

    private fun generateQuestions(quizQuestionViewModel: QuizQuestionViewModel, courseId : Int, firstQuestionLimit : Int, secondQuestionLimit : Int, thirdQuestionLimit : Int, callback: (ArrayList<QuizQuestionModel>?) -> Unit)
    {
        lifecycleScope.launch {
            try {
                coroutineScope {
                    val easyQuestionsDeferred = async { quizQuestionViewModel.getLastFiveQuestions(courseId, 1, firstQuestionLimit) }
                    val midQuestionsDeferred = async { quizQuestionViewModel.getLastFiveQuestions(courseId, 2, secondQuestionLimit) }
                    val hardQuestionsDeferred = async { quizQuestionViewModel.getLastFiveQuestions(courseId, 3, thirdQuestionLimit) }

                    val results = awaitAll(easyQuestionsDeferred, midQuestionsDeferred, hardQuestionsDeferred)
                    val questionList = results[0] + results[1] + results[2]

                    if (questionList.isNotEmpty()) {
                        myQuestionList?.addAll(questionList)
                        callback(myQuestionList)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
                    binding.btnSubmit.isEnabled = true
                }
            }

            R.id.textViewSecondOption -> {
                textViewSecondOption.let {
                    selectedOptionView(it, 2)
                    binding.btnSubmit.isEnabled = true
                }
            }

            R.id.textViewThirdOption -> {
                textViewThirdOption.let {
                    selectedOptionView(it, 3)
                    binding.btnSubmit.isEnabled = true
                }
            }

            R.id.textViewFourthOption -> {
                textViewFourthOption.let {
                    selectedOptionView(it, 4)
                    binding.btnSubmit.isEnabled = true
                }
            }
        }
    }
    private fun updateQuizQuestions(correctAnswers: Int, courseId: Int, timeSpent: Double) {
        when (correctAnswers)
        {
            5 ->
            {
                when (timeSpent) {
                    in 1.0..3.0 -> {
                        /** 3 3 3 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 0, 0, 5) {}
                    }
                    in 3.01..5.0 -> {
                        /** 2 3 3 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 0, 1, 4) {}
                    }
                    in 5.01..7.0 -> {
                        /** 2 2 3 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 0, 2, 3) {}
                    }
                    else -> {
                        /** 2 2 2 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 0, 3, 2) {}
                    }
                }
            }
            4 ->
            {
                when (timeSpent) {
                    in 1.0..5.0 -> {
                        /** 2 2 2 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 0, 3, 2) {}
                    }
                    in 5.01..7.0 -> {
                        /** 2 2 2 2 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 0, 4, 1) {}
                    }
                    in 7.01..9.0 -> {
                        /** 2 2 2 2 2 **/
                        generateQuestions(quizQuestionViewModel, courseId, 0, 5, 0) {}
                    }
                    else -> {
                        /** 1 2 3 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 1, 1, 3) {}
                    }
                }
            }
            3 ->
            {
                when (timeSpent) {
                    in 1.0..5.0 -> {
                        /** 1 2 3 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 1, 1, 3) {}
                    }
                    in 5.01..7.0 -> {
                        /** 1 2 2 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 1, 2, 2) {}
                    }
                    in 7.01..9.0 -> {
                        /** 1 2 2 2 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 1, 3, 1) {}
                    }
                    else -> {
                        /** 1 2 2 2 2 **/
                        generateQuestions(quizQuestionViewModel, courseId, 1, 4, 0) {}
                    }
                }
            }
            2 ->
            {
                when (timeSpent) {
                    in 1.0..5.0 -> {
                        /** 1 2 2 2 2**/
                        generateQuestions(quizQuestionViewModel, courseId, 1, 4, 0) {}
                    }
                    in 5.01..7.0 -> {
                        /** 1 1 3 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 1, 0, 3) {}
                    }
                    in 7.01..9.0 -> {
                        /** 1 1 2 3 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 2, 1, 2) {}
                    }
                    else -> {
                        /** 1 1 2 2 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 2, 2, 1) {}
                    }
                }
            }
            1 ->
            {
                when (timeSpent) {
                    in 1.0..5.0 -> {
                        /** 1 1 2 2 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 2, 2, 1) {}
                    }
                    in 5.01..7.0 -> {
                        /** 1 1 2 2 2 **/
                        generateQuestions(quizQuestionViewModel, courseId, 2, 3, 0) {}
                    }
                    in 7.01..9.0 -> {
                        /** 1 1 1 2 2 **/
                        generateQuestions(quizQuestionViewModel, courseId, 3, 2, 0) {}
                    }
                    else -> {
                        /** 1 1 1 1 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 4, 0, 1) {}
                    }
                }
            }
            0 ->
            {
                when (timeSpent) {
                    in 2.0..7.0 -> {
                        /** 1 1 1 1 3 **/
                        generateQuestions(quizQuestionViewModel, courseId, 4, 0, 1) {}
                    }
                    in 7.01..9.0 -> {
                        /** 1 1 1 1 2 **/
                        generateQuestions(quizQuestionViewModel, courseId, 4, 1, 0) {}
                    }
                    else -> {
                        /** 1 1 1 1 1 **/
                        generateQuestions(quizQuestionViewModel, courseId, 5, 0, 0) {}
                    }
                }
            }
        }
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}