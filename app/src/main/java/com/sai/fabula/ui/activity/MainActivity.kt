package com.sai.fabula.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sai.fabula.R
import com.sai.fabula.State
import com.sai.fabula.databinding.ActivityMainBinding
import com.sai.fabula.ui.adapter.NewsAdapter
import com.sai.fabula.utils.showToast
import com.sai.fabula.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModel()

    private val newsAdapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityViewBinding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = newsAdapter
        }

        initArticles()
    }

    private fun initArticles() {
        viewModel.newsLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    newsAdapter.setArticles(state.data)
                    showLoading(false)
                }
                is State.Error -> {
                    showToast(state.message)
                    showLoading(false)
                }
            }
        })

        activityViewBinding.swipeRefreshLayout.setOnRefreshListener {
            getNews()
        }

        if (viewModel.newsLiveData.value !is State.Success) {
            getNews()
        }
    }

    private fun getNews() {
        viewModel.getNews()
    }

    private fun showLoading(isLoading: Boolean) {
        activityViewBinding.swipeRefreshLayout.isRefreshing = isLoading
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
}
