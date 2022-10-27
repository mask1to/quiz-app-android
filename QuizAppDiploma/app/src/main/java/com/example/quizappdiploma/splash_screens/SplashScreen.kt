package com.example.quizappdiploma.splash_screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.quizappdiploma.MainActivity
import com.example.quizappdiploma.R


class SplashScreen : AppCompatActivity() {

    private lateinit var animationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        animationView = findViewById(R.id.animation_view)
        setupAnimation(animationView)

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
            }
        },2000)
    }


    @SuppressLint("Range")
    private fun setupAnimation(animationView: LottieAnimationView)
    {
        animationView.speed = 1.0F // How fast does the animation play
        animationView.progress = 50F // Starts the animation from 50% of the beginning
        animationView.addAnimatorUpdateListener {

        }
        animationView.repeatMode = LottieDrawable.RESTART // Restarts the animation (you can choose to reverse it as well)
        animationView.cancelAnimation()
    }
}