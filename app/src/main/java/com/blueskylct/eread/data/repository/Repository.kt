package com.blueskylct.eread.data.repository

import androidx.lifecycle.LiveData
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.domain.model.CacheBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {

    companion object{
        private var instance: Repository? = null

        fun getInstance(): Repository{
            return instance?: synchronized(this){
                instance?: Repository().also {
                    instance = it
                }
            }
        }
    }

    private val bookDao = MyApplication.getDatabase().bookDao()

    suspend fun getBook(): LiveData<ArrayList<CacheBook>>
    = withContext(Dispatchers.IO){
        bookDao.getBook()
    }

}