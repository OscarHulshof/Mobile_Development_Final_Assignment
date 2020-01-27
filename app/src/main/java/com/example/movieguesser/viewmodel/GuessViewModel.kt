package com.example.movieguesser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.movieguesser.data.MovieRepository
import com.example.movieguesser.model.Movie
import com.example.movieguesser.model.MovieList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuessViewModel(application: Application) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val movieRepository = MovieRepository(application.applicationContext)
    private val maxNumberOfMovies = 3

    // Values for view
    var correctMovie = MutableLiveData<Movie>()
    val movies = MutableLiveData<List<Movie>>()
    val error = MutableLiveData<String>()

    fun getMoviesFromApi(page: Int = 1, difficulty: String) {
        movieRepository.getPopularMoviesFromApi(page).enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful) {
                    val movieList = response.body()

                    // Shuffle list to be less predictable
                    if (movieList != null) {
                        movieList.movies = movieList.movies.shuffled()
                    }

                    selectCorrectMovie(movieList)
                    // If no suitable movie is found, increment page count and run function again
                    if (correctMovie.value == null) getMoviesFromApi(page + 1, difficulty)
                    // Add correct movie to view
                    else movies.value = listOf(correctMovie.value!!)

                    when (difficulty) {
                        "EASY" -> selectEasyMovies(movieList)
                        "HARD" -> selectHardMovies()
                    }
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                error.value = t.message
            }
        })
    }

    // Get correct movie and avoid movies that the user recently encountered
    // If a movie is not found in database, select it and break out foreach loop
    private fun selectCorrectMovie(movieList: MovieList?) {
        if (movieList != null) {
            for (movie in movieList.movies) {
                if (!hasMovieRecentlyBeenSelected(movie)) {
                    correctMovie.value = movie
                    break
                }
            }
        }
    }

    private fun selectEasyMovies(movieList: MovieList?) {
        if (correctMovie.value != null && movieList != null) {
            for (movie in movieList.movies) {
                if (movie.id != correctMovie.value!!.id) {
                    movies.value = movies.value?.plus(movie)
                    if (movies.value?.size == maxNumberOfMovies) break
                }
            }
        }

        // Randomize collection order so that the first answer in the view won't always be correct
        movies.value = movies.value?.shuffled()
    }

    private fun selectHardMovies() {
        // If difficulty is set to hard, get similar movies from the API.
        movieRepository.getSimilarMoviesFromApi(correctMovie.value!!.id)
            .enqueue(object : Callback<MovieList> {
                override fun onResponse(
                    call: Call<MovieList>,
                    response: Response<MovieList>
                ) {
                    // Fill movies with similar movies to the correct movie
                    if (response.isSuccessful) {
                        // Get first two movies
                        val similarMovies = response.body()?.movies?.subList(0, 2)
                        movies.value = similarMovies?.let { movies.value?.plus(it) }

                        // Randomize collection order so that the first answer in the view won't always be correct
                        movies.value = movies.value?.shuffled()
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    error.value = t.message
                }
            })
    }

    // Add correct movie to database to avoid getting the same movie again for a while
    fun addMovieToDb() {
        ioScope.launch { movieRepository.insertMovieIntoDb(correctMovie.value!!) }
    }

    /*
    Checks if movie is stored in database already and returns true if found.
    Otherwise returns false
    */
    private fun hasMovieRecentlyBeenSelected(movie: Movie): Boolean {
        val recentMovies = movieRepository.getRecentMoviesFromDb()

        if (!recentMovies.isNullOrEmpty()) {
            recentMovies.forEach {
                if (movie.id == it.id) {
                    return true
                }
            }
        }
        return false
    }
}