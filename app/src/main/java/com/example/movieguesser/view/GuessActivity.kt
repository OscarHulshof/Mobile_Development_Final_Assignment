package com.example.movieguesser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieguesser.R
import com.example.movieguesser.model.Movie
import com.example.movieguesser.viewmodel.GuessViewModel
import kotlinx.android.synthetic.main.activity_guess.*

class GuessActivity : AppCompatActivity() {
    private lateinit var correctMovie: Movie
    private var movies = arrayListOf<Movie>()
    private val movieAdapter =
        GuessMovieAdapter(movies) { movie -> onMovieClicked(movie) }
    private lateinit var viewModel: GuessViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        rvMovies.layoutManager =
            GridLayoutManager(this@GuessActivity, 3, RecyclerView.VERTICAL, false)
        rvMovies.adapter = movieAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(GuessViewModel::class.java)

        viewModel.movies.observe(this, Observer {
            if (it != null) {
                movies.clear()
                movies.addAll(it)
                movieAdapter.notifyDataSetChanged()
            }
        })

        viewModel.correctMovie.observe(this, Observer {
            correctMovie = it
            correctMovie.censorOverviewText()
            tvMovieDescription.text = correctMovie.overview
        })

        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        viewModel.getMoviesFromApi(difficulty = intent.getStringExtra(getString(R.string.difficulty))!!)
    }

    private fun onMovieClicked(movie: Movie) {
        if (movie.id == correctMovie.id) {
            Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.incorrect), Toast.LENGTH_SHORT).show()
        }

        correctMovie.answer = movie.title
        correctMovie.timestamp = System.currentTimeMillis()
        viewModel.addMovieToDb()
        viewModel.getMoviesFromApi(difficulty = intent.getStringExtra(getString(R.string.difficulty))!!)
    }

    // Go back to home screen when back button is pressed
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
