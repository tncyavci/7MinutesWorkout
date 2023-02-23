package com.tuncayavci.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import com.tuncayavci.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? =
        null //Variable for Rest down timer
    private var restProgressBar =
        0 // variable for timer progress

    private var exerciseTimer: CountDownTimer? =
        null // variable for exercise timer
    private var exerciseProgressBar =
        0 // variable for exercise timer progress

    private var exerciseTimerDuration: Long = 30

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        tts = TextToSpeech(this,this)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        exerciseList = Constants.defaultExercises()

        setupRestView()
    }

    private fun setupRestView() {

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE

        /**
         * Here firstly we will check if the timer is running the and it is not null then cancel the running timer and start the new one.
         * And set the progress to initial which is 0.
         */
        if (restTimer != null) {
            restTimer?.cancel()
            restProgressBar = 0
        }


        binding?.tvUpcomingExercise?.text = exerciseList!![currentExercisePosition + 1].getName()
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
                currentExercisePosition++
                setupExerciseView()
            }
        }.start()

    }

    private fun setupExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.GONE
        binding?.tvUpcomingExercise?.visibility = View.GONE


        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgressBar = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
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

                if (currentExercisePosition < (exerciseList?.size!! - 1)) {
                    setupRestView()
                } else {
                    Toast.makeText(
                        this@ExerciseActivity,
                        "Congratulations! You have completed the 7 minutes workout",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language specified is not supported!")
            }
        }else{
            Log.e("TTS","Initialization Failed!")
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null) {
            restTimer?.cancel()
            restProgressBar = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgressBar = 0
        }

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        binding = null
    }
}