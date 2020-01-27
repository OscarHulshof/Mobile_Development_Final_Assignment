package com.example.movieguesser.data.api

import com.example.movieguesser.model.MovieList
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/popular?api_key=c24f28a36bef9d0cbba92cd2cd01b244")
    fun getCurrentPopularMovies(
        @Query("page") page: Int
    ): Call<MovieList>

    @GET("movie/{movie_id}/recommendations?api_key=c24f28a36bef9d0cbba92cd2cd01b244")
    fun getSimilarMoviesForId(
        @Path(
            value = "movie_id",
            encoded = true
        ) movieId: String
    ): Call<MovieList>
}