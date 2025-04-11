package com.blueskylct.eread.ui.reading

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.utils.EpubUtil
import kotlinx.coroutines.launch

class ReadingViewModel: ViewModel() {

    private val _chapterListLiveData = MutableLiveData<List<String>>()
    val chapterListLiveData get() = _chapterListLiveData

    init {
        _chapterListLiveData.value = ArrayList()
    }

    fun loadChapter(){
        viewModelScope.launch {
            _chapterListLiveData.value = EpubUtil.getChapter(MyApplication.getInstance().getBook())
        }
    }

}