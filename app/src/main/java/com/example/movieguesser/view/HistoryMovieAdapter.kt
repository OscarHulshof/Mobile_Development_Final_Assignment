package com.example.movieguesser.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieguesser.R
import com.example.movieguesser.model.Movie
import kotlinx.android.synthetic.main.item_movie_history.view.*

class HistoryMovieAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<HistoryMovieAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_movie_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            Glide.with(context).load(movie.getPosterUrl())
                .into(itemView.ivMovie)
            itemView.tvTitle.text = movie.title
            itemView.tvDescription.text = movie.overview
            itemView.tvAnswer.text = movie.answer
        }
    }
}