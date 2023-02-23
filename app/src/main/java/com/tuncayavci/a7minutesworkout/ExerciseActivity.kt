package com.tuncayavci.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.tuncayavci.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? =
        null //Variable for Rest down timer
    private var restProgressBar =
        0 // variable for timer progress

    private var exerciseTimer: CountDownTimer? =
        null // variable for exercise timer
    private var exerciseProgressBar =
        0 // variable for exercise timer progress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRestView()
    }

    private fun setupRestView() {

        /**
         * Here firstly we will check if the timer is running the and it is not null then cancel the running timer and start the new one.
         * And set the progress to initial which is 0.
         */
        if (restTimer != null) {
            restTimer?.cancel()
            restProgressBar = 0
        }

        // This function is used to set the progress details.
        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress =
            restProgressBar // set the current progressbar to the specified value

        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgressBar++ // it is increased by 1
                binding?.progressBar?.progress =
                    10 - restProgressBar // indicates progress bar progress
                binding?.tvTimer?.text =
                    (10 - restProgressBar).toString() // current progress is set to text view
            }

            override fun onFinish() {
                // when 10 seconds will complete this function will be executed
                setupExerciseView()
            }
        }.start()

    }

    private fun setupExerciseView() {
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.tvTitle?.text = "EXERCISE NAME"
        binding?.flProgressBarExercise?.visibility = View.VISIBLE

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgressBar = 0
        }

        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgressBar
        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgressBar++
                binding?.progressBarExercise?.progress = 30 - exerciseProgressBar
                binding?.tvTimerExercise?.text = (30 - exerciseProgressBar).toString()
            }

            override fun onFinish() {
                Toast.makeText(
                    this@ExerciseActivity,
                    "This is 30 seconds completed so now we will add all the exercises.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }

    override fun onDestroy() {
        if (restTimer != null) {
            restTimer?.cancel()
            restProgressBar = 0
        }
        super.onDestroy()
        binding = null
    }
}