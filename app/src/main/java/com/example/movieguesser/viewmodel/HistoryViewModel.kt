package com.example.movieguesser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.movieguesser.data.MovieRepository
import com.example.movieguesser.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val movieRepository = MovieRepository(application.applicationContext)
    var correct = movieRepository.getNumberOfCorrectGuesses()
    var incorrect = movieRepository.getNumberOfIncorrectGuesses()
    var recentMovies = MutableLiveData<List<Movie>>()

    fun getRecentMovies() {
        val movies = movieRepository.getRecentMoviesFromDb()
        recentMovies.value = movies
    }

    fun deleteHistory() {
        ioScope.launch {
            movieRepository.deleteAllMoviesFromDb()
        }
    }
}