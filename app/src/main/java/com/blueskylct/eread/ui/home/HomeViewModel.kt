package com.blueskylct.eread.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.siegmann.epublib.domain.Book

class HomeViewModel: ViewModel() {
    private val _bookList = MutableLiveData<ArrayList<Book>>()
    val bookList get() = _bookList.value

    init {
        _bookList.value = ArrayList()
    }
}