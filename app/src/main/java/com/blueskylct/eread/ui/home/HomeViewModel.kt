package com.blueskylct.eread.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueskylct.eread.data.repository.Repository
import com.blueskylct.eread.domain.model.CacheBook
import kotlinx.coroutines.launch


class HomeViewModel: ViewModel() {

    private val _bookListLiveData = MutableLiveData<List<CacheBook>>()
    val bookListLiveData get() = _bookListLiveData

    fun loadBook(){
        viewModelScope.launch {
            Repository.getInstance().getBook().let {
                _bookListLiveData.value = it
            }
        }
        if (_bookListLiveData.value.isNullOrEmpty())
            _bookListLiveData.value = ArrayList()
    }
}