package com.example.movieguesser.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieguesser.R
import com.example.movieguesser.model.Movie
import kotlinx.android.synthetic.main.item_movie_guess.view.*

class GuessMovieAdapter(private val movies: List<Movie>, private val onClick: (Movie) -> Unit) :
    RecyclerView.Adapter<GuessMovieAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val limit: Int = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_movie_guess, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        // Limit maximum number of items allowed in recyclerview
        return if (movies.size > limit) {
            limit
        } else {
            movies.size
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onClick(movies[adapterPosition])
            }
        }

        fun bind(movie: Movie) {
            Glide.with(context).load(movie.getPosterUrl())
                .into(itemView.ivMovie)
            itemView.tvMovie.text = movie.title
        }
    }
}