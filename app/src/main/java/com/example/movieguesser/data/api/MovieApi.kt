package com.example.movieguesser.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieApi {
    companion object {
        private const val baseUrl = "https://api.themoviedb.org/3/"

        fun createApi(): MovieApiService {
            // Create HTTP client to log network traffic
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            //create Retrofit instance
            val movieApi = Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()

            return movieApi.create(MovieApiService::class.java)
        }
    }
}

