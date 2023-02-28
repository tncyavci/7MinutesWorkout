package com.tuncayavci.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tuncayavci.a7minutesworkout.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {

    private var binding: ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener { view ->
            view?.let {
                onBackPressed()
            }
        }
        binding?.btnFinish?.setOnClickListener {
            finish()
        }
    }
}