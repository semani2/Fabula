package com.sai.fabula.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sai.fabula.R
import com.sai.fabula.State
import com.sai.fabula.databinding.ActivityMainBinding
import com.sai.fabula.ui.adapter.NewsAdapter
import com.sai.fabula.utils.showToast
import com.sai.fabula.viewmodel.MainViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val newsAdapter = NewsAdapter()

    private lateinit var activityViewBinding: ActivityMainBinding

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        activityViewBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(activityViewBinding.root)

        activityViewBinding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = newsAdapter
        }

        val disposable = newsAdapter.getClickObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                openUrl(it)
            }
        compositeDisposable.add(disposable)

        initArticles()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }

            else -> true
        }
    }

    override fun onBackPressed() {
        MaterialDialog.Builder(this)
            .setTitle("Exit?")
            .setMessage("Are you sure want to exit?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                dialogInterface.dismiss()
                super.onBackPressed()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .build()
            .show()
    }

    private fun openUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
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
}
