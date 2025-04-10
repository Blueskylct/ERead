package com.blueskylct.eread.ui.reading

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReadingViewModel: ViewModel() {

    private val _chapterListLiveData = MutableLiveData<List<String>>()
    val chapterListLiveData get() = _chapterListLiveData

    init {
        _chapterListLiveData.value = ArrayList()
    }
}