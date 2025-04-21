package com.blueskylct.eread.ui.reading

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.utils.EpubUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadingViewModel: ViewModel() {

    private val _chapterListLiveData = MutableLiveData<List<String>>()
    val chapterListLiveData get() = _chapterListLiveData

    private val _chapterContentLiveData = MutableLiveData<String>()
    val chapterContentLiveData get() = _chapterContentLiveData

    private var _index = 0
    val index get() = _index

    init {
        _chapterListLiveData.value = ArrayList()
    }

    fun loadChapters(){
        _chapterListLiveData.value =
            EpubUtil.getChapter(MyApplication.getInstance().getBook())
    }

    fun setContent(content: String){
        viewModelScope.launch {
            _chapterContentLiveData.value = content
        }
    }

    fun setIndex(index: Int){
        _index = index
    }

}