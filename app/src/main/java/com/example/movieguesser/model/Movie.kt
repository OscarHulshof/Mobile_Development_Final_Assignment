package com.example.movieguesser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "movieTable")
data class Movie(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id") var id: String,

    // Url for movie poster
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path") var posterImageUrl: String,

    @ColumnInfo(name = "title")
    @SerializedName("title") var title: String,

    // Short summary of movie plot
    @ColumnInfo(name = "overview")
    @SerializedName("overview") var overview: String,

    // Answer the user guessed
    @ColumnInfo(name = "answer") var answer: String?,

    // Timestamp to get most recent movies from database
    @ColumnInfo(name = "timestamp") var timestamp: Long
) : Parcelable {
    fun getPosterUrl() = "https://image.tmdb.org/t/p/original$posterImageUrl"

    // Censor text by removing all words from title that are longer than 3 characters
    fun censorOverviewText() {
        // Get words from title
        val titleWordList =
            this.title.trim().splitToSequence(' ', ':').filter { it.length > 3 }.toList()

        titleWordList.forEach {
            val titleWord = it.toLowerCase(Locale.ENGLISH)
            val lowerCaseText = this.overview.toLowerCase(Locale.ENGLISH)
            this.overview = lowerCaseText.replace(titleWord, "_")
        }
    }
}