package com.blueskylct.eread.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.domain.model.CacheBook
import kotlinx.coroutines.launch


class HomeViewModel: ViewModel() {

    private val _bookList = MutableLiveData<ArrayList<CacheBook>>()
    val bookList get() = _bookList.value

    fun initBook(){
        viewModelScope.launch {
            _bookList.value = MyApplication.getRepository().getBook().value
        }
    }

}