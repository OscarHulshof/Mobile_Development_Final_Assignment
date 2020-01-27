package com.example.movieguesser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieguesser.R
import com.example.movieguesser.model.Movie
import com.example.movieguesser.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    private var movies = arrayListOf<Movie>()
    private val movieAdapter = HistoryMovieAdapter(movies)
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        rvRecentMovies.layoutManager =
            GridLayoutManager(this@HistoryActivity, 1, RecyclerView.VERTICAL, false)
        rvRecentMovies.adapter = movieAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        viewModel.recentMovies.observe(this, Observer {
            if (it != null) {
                movies.clear()
                movies.addAll(it)
                movieAdapter.notifyDataSetChanged()
            }
        })

        viewModel.correct.observe(this, Observer {
            if (it != null) {
                tvCorrect.text = it.toString()
            }
        })

        viewModel.incorrect.observe(this, Observer {
            if (it != null) {
                tvIncorrect.text = it.toString()
            }
        })

        viewModel.getRecentMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_history -> {
                viewModel.deleteHistory()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
