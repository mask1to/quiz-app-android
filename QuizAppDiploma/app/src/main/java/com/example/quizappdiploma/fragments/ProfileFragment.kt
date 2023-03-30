import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.quizappdiploma.R
import com.example.quizappdiploma.databinding.FragmentProfileBinding
import com.example.quizappdiploma.preferences.PreferenceManager
import androidx.navigation.fragment.findNavController
import com.example.quizappdiploma.fragments.ProfileFragmentDirections
import com.example.quizappdiploma.fragments.entities.StudentFragmentDirections

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileAnimation: LottieAnimationView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileAnimation = binding.cowAnimation
        userName = binding.userNameTxt
        userEmail = binding.userEmail

        setupAnimation(profileAnimation)

        val loggedInUser = preferenceManager.getLoggedInUser()
        if (loggedInUser != null) {
            userName.text = loggedInUser.username
            userEmail.text = loggedInUser.email
        } else {
            Toast.makeText(requireContext(), "Not logged in", Toast.LENGTH_SHORT).show()
        }

        binding.courseBtn.setOnClickListener {
            val action = StudentFragmentDirections.actionStudentFragmentToCourseFragment()
            findNavController().navigate(action)
        }
    }

    @SuppressLint("Range")
    private fun setupAnimation(animationView: LottieAnimationView) {
        animationView.speed = 1.0F // How fast does the animation play
        animationView.progress = 35F // Starts the animation from 50% of the beginning
        animationView.setAnimation(R.raw.fitnesscow)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
