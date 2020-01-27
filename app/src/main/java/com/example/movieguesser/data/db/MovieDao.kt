package com.example.movieguesser.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movieguesser.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movieTable ORDER BY timestamp DESC LIMIT 20")
    fun getRecentMovies(): List<Movie>

    @Query("SELECT COUNT(id) FROM movieTable WHERE title == answer")
    fun getNumberOfCorrectGuesses(): LiveData<Int>

    @Query("SELECT COUNT(id) FROM movieTable WHERE title != answer")
    fun getNumberOfIncorrectGuesses(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Query("DELETE FROM movieTable")
    suspend fun deleteAllMovies()
}