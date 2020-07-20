package com.sai.fabula.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.fabula.database.NewsRepository
import com.sai.fabula.utils.ArticleListState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsLiveData = MutableLiveData<ArticleListState>()

    val newsLiveData: LiveData<ArticleListState>
        get() = _newsLiveData

    fun getNews() {
        viewModelScope.launch {
            newsRepository.getNews().collect {
                _newsLiveData.value = it
            }
        }
    }
}
