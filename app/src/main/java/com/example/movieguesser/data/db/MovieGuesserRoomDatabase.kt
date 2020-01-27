package com.example.movieguesser.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieguesser.model.Movie

@Database(entities = [Movie::class], version = 4, exportSchema = false)
abstract class MovieGuesserRoomDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private const val DATABASE_NAME = "MOVIE_GUESSER_DATABASE"

        @Volatile
        private var MovieGuesserRoomDatabaseInstance: MovieGuesserRoomDatabase? = null

        fun getDatabase(context: Context): MovieGuesserRoomDatabase? {
            if (MovieGuesserRoomDatabaseInstance == null) {
                synchronized(MovieGuesserRoomDatabase::class.java) {
                    if (MovieGuesserRoomDatabaseInstance == null) {
                        MovieGuesserRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            MovieGuesserRoomDatabase::class.java,
                            DATABASE_NAME
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return MovieGuesserRoomDatabaseInstance
        }
    }
}