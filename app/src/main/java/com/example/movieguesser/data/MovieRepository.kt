package com.example.movieguesser.data

import android.content.Context
import com.example.movieguesser.data.api.MovieApi
import com.example.movieguesser.data.db.MovieGuesserRoomDatabase
import com.example.movieguesser.model.Movie

class MovieRepository(context: Context) {
    private val movieApi = MovieApi.createApi()
    private val movieGuesserRoomDatabase = MovieGuesserRoomDatabase.getDatabase(context)
    private val movieDao = movieGuesserRoomDatabase!!.movieDao()

    // Api functions
    fun getPopularMoviesFromApi(page: Int) = movieApi.getCurrentPopularMovies(page)

    fun getSimilarMoviesFromApi(movieId: String) = movieApi.getSimilarMoviesForId(movieId)

    // Database functions
    fun getRecentMoviesFromDb() = movieDao.getRecentMovies()

    fun getNumberOfCorrectGuesses() = movieDao.getNumberOfCorrectGuesses()

    fun getNumberOfIncorrectGuesses() = movieDao.getNumberOfIncorrectGuesses()

    suspend fun insertMovieIntoDb(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    suspend fun deleteAllMoviesFromDb() {
        movieDao.deleteAllMovies()
    }
}