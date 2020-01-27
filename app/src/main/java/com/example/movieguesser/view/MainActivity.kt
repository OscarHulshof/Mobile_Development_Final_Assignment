package com.example.movieguesser.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieguesser.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        btnEasy.setOnClickListener {
            startGuessActivity(getString(R.string.easy))
        }
        btnHard.setOnClickListener {
            startGuessActivity(getString(R.string.hard))
        }
        btnStats.setOnClickListener {
            startStatsActivity()
        }
    }

    private fun startGuessActivity(difficulty: String) {
        val intent = Intent(this, GuessActivity::class.java)
        intent.putExtra(getString(R.string.difficulty), difficulty)
        startActivity(intent)
    }

    private fun startStatsActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
}
