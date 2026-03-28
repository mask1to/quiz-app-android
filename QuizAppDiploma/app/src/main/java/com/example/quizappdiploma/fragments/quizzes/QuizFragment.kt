package com.example.quizappdiploma.fragments.quizzes

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
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

class QuizFragment : Fragment(), OnClickListener {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var quizQuestionViewModel: QuizQuestionViewModel
    private lateinit var userAnswersViewModel: UserAnswersViewModel
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var quizStatsViewModel: QuizStatsViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var submitBtn: Button
    private lateinit var textViewProgress: TextView
    private lateinit var textViewQuestion: TextView
    private lateinit var imageQuestion: ImageView
    private lateinit var textViewFirstOption: TextView
    private lateinit var textViewSecondOption: TextView
    private lateinit var textViewThirdOption: TextView
    private lateinit var textViewFourthOption: TextView
    private lateinit var questionW: TextView
    private lateinit var picasso: Picasso
    private lateinit var preferenceManager: PreferenceManager

    private var additionalQuestionsGenerated = false
    private var questionStartTime: Double = 0.0
    private var myCurrentPosition: Int = 1
    private var myQuestionList: ArrayList<QuizQuestionModel>? = null
    private var mySelectedOption: Int = 0
    private var correctAnswers: Int = 0
    private val REQUEST_CODE_PERMISSIONS = 1000

    private var timerJob: Job? = null
    private var timerSeconds: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
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

        if (!hasPermissions()) {
            requestPermissions()
        } else {
            val cacheSize = 20 * 1024 * 1024
            val cache = Cache(requireContext().cacheDir, cacheSize.toLong())
            val okHttpClient = OkHttpClient.Builder().cache(cache).build()
            picasso = Picasso.Builder(requireContext())
                .downloader(OkHttp3Downloader(okHttpClient))
                .indicatorsEnabled(false)
                .loggingEnabled(true)
                .build()
        }

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
            if (myQuestionList == null) myQuestionList = ArrayList()
            myQuestionList?.addAll(firstQuestions)
            binding.btnSubmit.isEnabled = false
            setQuestion()
        }

        binding.btnSubmit.setOnClickListener {
            val questionEndTime = System.currentTimeMillis()
            val timeSpent = questionEndTime - questionStartTime
            val timeSpentSeconds = timeSpent / 1000
            questionStartTime = questionEndTime.toDouble()

            if (mySelectedOption == 0) {
                binding.btnSubmit.isEnabled = false
                val previousQuestion = myQuestionList!![myCurrentPosition - 1]
                quizQuestionViewModel.updateQuestion(previousQuestion)
                myCurrentPosition++

                when {
                    myCurrentPosition <= myQuestionList!!.size -> setQuestion()
                    else -> {
                        quizViewModel.getAllQuizPropertiesByCourseId(courseId).observeOnce(viewLifecycleOwner) { quizIds ->
                            if (!quizIds.isNullOrEmpty()) {
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
                                timerJob?.cancel()
                                val action = QuizFragmentDirections.actionQuizFragmentToResultQuizFragment(
                                    loggedUser.username.toString(), myQuestionList!!.size, correctAnswers
                                )
                                Navigation.findNavController(requireView()).navigate(action)
                            }
                        }
                    }
                }
            } else {
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

                if (question!!.answer != mySelectedOption) {
                    answerView(mySelectedOption, R.drawable.bg_answer_wrong)
                    // Also highlight correct answer
                    answerView(question.answer ?: 0, R.drawable.bg_answer_correct)
                } else {
                    answerView(mySelectedOption, R.drawable.bg_answer_correct)
                    correctAnswers++
                }

                disableOptions()

                submitBtn.text = if (myCurrentPosition == myQuestionList!!.size) "Finish" else "Next"
                mySelectedOption = 0

                if (myCurrentPosition == 5 && !additionalQuestionsGenerated) {
                    additionalQuestionsGenerated = true
                    viewLifecycleOwner.lifecycleScope.launch {
                        val averageTime = quizQuestionViewModel.getAverageTimeSpentOnUsedQuestions()
                        updateQuizQuestions(correctAnswers, courseId, averageTime!!)
                    }
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerSeconds = 0
        timerJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                delay(1000)
                timerSeconds++
                binding.timerText.text = "${timerSeconds}s"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {
        defaultOptionsView()
        val question: QuizQuestionModel = myQuestionList!![myCurrentPosition - 1]

        progressBar.progress = myCurrentPosition
        progressBar.max = myQuestionList!!.size
        textViewProgress.text = "Question $myCurrentPosition/${myQuestionList!!.size}"

        val points = question.questionPoints ?: 1
        questionW.text = "$points ${if (points == 1) "point" else "points"}"

        updateDifficultyBadge(question.questionDifficulty)

        val imagePath = question.image_path
        if (!imagePath.isNullOrEmpty()) {
            binding.imageQuestion.visibility = View.VISIBLE
            binding.imageProgressBar2.visibility = View.VISIBLE
            Picasso.get()
                .load(imagePath)
                .noFade()
                .into(imageQuestion, object : Callback {
                    override fun onSuccess() {
                        binding.imageProgressBar2.visibility = View.GONE
                    }
                    override fun onError(e: Exception?) {
                        binding.imageProgressBar2.visibility = View.GONE
                        binding.imageQuestion.visibility = View.GONE
                        Log.e("QuizFragment", "Error loading image: ${e?.message}")
                    }
                })
        } else {
            binding.imageQuestion.visibility = View.GONE
            binding.imageProgressBar2.visibility = View.GONE
        }

        textViewQuestion.text = question.questionName
        textViewFirstOption.text = question.questionOptionA
        textViewSecondOption.text = question.questionOptionB
        textViewThirdOption.text = question.questionOptionC
        textViewFourthOption.text = question.questionOptionD

        binding.btnSubmit.text = "Submit"
        questionStartTime = System.currentTimeMillis().toDouble()

        enableOptions()
        startTimer()
    }

    private fun updateDifficultyBadge(difficulty: Int?) {
        val ctx = requireContext()
        when (difficulty) {
            1 -> {
                binding.difficultyBadge.text = "Easy"
                binding.difficultyBadge.setTextColor(ContextCompat.getColor(ctx, R.color.difficulty_easy))
                binding.difficultyBadge.background = ContextCompat.getDrawable(ctx, R.drawable.bg_chip_difficulty_easy)
            }
            2 -> {
                binding.difficultyBadge.text = "Medium"
                binding.difficultyBadge.setTextColor(ContextCompat.getColor(ctx, R.color.difficulty_medium))
                binding.difficultyBadge.background = ContextCompat.getDrawable(ctx, R.drawable.bg_chip_difficulty_medium)
            }
            3 -> {
                binding.difficultyBadge.text = "Hard"
                binding.difficultyBadge.setTextColor(ContextCompat.getColor(ctx, R.color.difficulty_hard))
                binding.difficultyBadge.background = ContextCompat.getDrawable(ctx, R.drawable.bg_chip_difficulty_hard)
            }
            else -> {
                binding.difficultyBadge.text = "Easy"
                binding.difficultyBadge.setTextColor(ContextCompat.getColor(ctx, R.color.difficulty_easy))
                binding.difficultyBadge.background = ContextCompat.getDrawable(ctx, R.drawable.bg_chip_difficulty_easy)
            }
        }
    }

    private fun defaultOptionsView() {
        listOf(textViewFirstOption, textViewSecondOption, textViewThirdOption, textViewFourthOption).forEach { option ->
            option.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_on_surface))
            option.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_answer_default)
        }
    }

    private fun selectedOptionView(txtView: TextView, selectedOption: Int) {
        defaultOptionsView()
        mySelectedOption = selectedOption
        txtView.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_primary))
        txtView.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_answer_selected)
    }

    private fun answerView(answer: Int, drawableRes: Int) {
        val target = when (answer) {
            1 -> textViewFirstOption
            2 -> textViewSecondOption
            3 -> textViewThirdOption
            4 -> textViewFourthOption
            else -> return
        }
        target.background = ContextCompat.getDrawable(requireContext(), drawableRes)
    }

    private fun generateQuestions(
        quizQuestionViewModel: QuizQuestionViewModel,
        courseId: Int,
        firstQuestionLimit: Int,
        secondQuestionLimit: Int,
        thirdQuestionLimit: Int,
        callback: (ArrayList<QuizQuestionModel>?) -> Unit
    ) {
        lifecycleScope.launch {
            try {
                coroutineScope {
                    val easyDeferred = async { quizQuestionViewModel.getLastFiveQuestions(courseId, 1, firstQuestionLimit) }
                    val midDeferred = async { quizQuestionViewModel.getLastFiveQuestions(courseId, 2, secondQuestionLimit) }
                    val hardDeferred = async { quizQuestionViewModel.getLastFiveQuestions(courseId, 3, thirdQuestionLimit) }
                    val results = awaitAll(easyDeferred, midDeferred, hardDeferred)
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.textViewFirstOption -> { selectedOptionView(textViewFirstOption, 1); binding.btnSubmit.isEnabled = true }
            R.id.textViewSecondOption -> { selectedOptionView(textViewSecondOption, 2); binding.btnSubmit.isEnabled = true }
            R.id.textViewThirdOption -> { selectedOptionView(textViewThirdOption, 3); binding.btnSubmit.isEnabled = true }
            R.id.textViewFourthOption -> { selectedOptionView(textViewFourthOption, 4); binding.btnSubmit.isEnabled = true }
        }
    }

    private fun updateQuizQuestions(correctAnswers: Int, courseId: Int, averageTimeSpent: Double) {
        when (correctAnswers) {
            5 -> when (averageTimeSpent) {
                in 1.0..4.0 -> generateQuestions(quizQuestionViewModel, courseId, 0, 0, 5) {}
                in 4.01..7.0 -> generateQuestions(quizQuestionViewModel, courseId, 0, 1, 4) {}
                in 7.01..10.0 -> generateQuestions(quizQuestionViewModel, courseId, 0, 2, 3) {}
                else -> generateQuestions(quizQuestionViewModel, courseId, 0, 3, 2) {}
            }
            4 -> when (averageTimeSpent) {
                in 1.0..5.0 -> generateQuestions(quizQuestionViewModel, courseId, 0, 3, 2) {}
                in 5.01..8.0 -> generateQuestions(quizQuestionViewModel, courseId, 0, 4, 1) {}
                in 8.01..11.0 -> generateQuestions(quizQuestionViewModel, courseId, 0, 5, 0) {}
                else -> generateQuestions(quizQuestionViewModel, courseId, 2, 2, 1) {}
            }
            3 -> when (averageTimeSpent) {
                in 1.0..6.0 -> generateQuestions(quizQuestionViewModel, courseId, 1, 1, 3) {}
                in 6.01..9.0 -> generateQuestions(quizQuestionViewModel, courseId, 1, 2, 2) {}
                in 9.01..12.0 -> generateQuestions(quizQuestionViewModel, courseId, 1, 3, 1) {}
                else -> generateQuestions(quizQuestionViewModel, courseId, 1, 4, 0) {}
            }
            2 -> when (averageTimeSpent) {
                in 1.0..7.0 -> generateQuestions(quizQuestionViewModel, courseId, 2, 0, 3) {}
                in 7.01..10.0 -> generateQuestions(quizQuestionViewModel, courseId, 2, 1, 2) {}
                in 10.01..13.0 -> generateQuestions(quizQuestionViewModel, courseId, 2, 2, 1) {}
                else -> generateQuestions(quizQuestionViewModel, courseId, 1, 4, 0) {}
            }
            1 -> when (averageTimeSpent) {
                in 1.0..8.0 -> generateQuestions(quizQuestionViewModel, courseId, 2, 2, 1) {}
                in 8.01..11.0 -> generateQuestions(quizQuestionViewModel, courseId, 2, 3, 0) {}
                in 11.01..14.0 -> generateQuestions(quizQuestionViewModel, courseId, 3, 2, 0) {}
                else -> generateQuestions(quizQuestionViewModel, courseId, 4, 1, 0) {}
            }
            0 -> when (averageTimeSpent) {
                in 1.0..9.0 -> generateQuestions(quizQuestionViewModel, courseId, 1, 4, 0) {}
                in 9.01..12.0 -> generateQuestions(quizQuestionViewModel, courseId, 4, 1, 0) {}
                else -> generateQuestions(quizQuestionViewModel, courseId, 5, 0, 0) {}
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

    private fun disableOptions() {
        textViewFirstOption.setOnClickListener(null)
        textViewSecondOption.setOnClickListener(null)
        textViewThirdOption.setOnClickListener(null)
        textViewFourthOption.setOnClickListener(null)
    }

    private fun enableOptions() {
        textViewFirstOption.setOnClickListener(this)
        textViewSecondOption.setOnClickListener(this)
        textViewThirdOption.setOnClickListener(this)
        textViewFourthOption.setOnClickListener(this)
    }

    private fun hasPermissions(): Boolean {
        val ctx = requireContext()
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isEmpty() || !grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(requireContext(), "Internet and storage permissions are required.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        timerJob?.cancel()
        super.onDestroyView()
        _binding = null
    }
}
