package com.blueskylct.eread.data.repository

import androidx.lifecycle.LiveData
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.data.local.AppDatabase
import com.blueskylct.eread.domain.model.CacheBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

    private val bookDao = AppDatabase.getInstance(MyApplication.getInstance()).bookDao()

    suspend fun insertBook(book: CacheBook)
    = withContext(Dispatchers.IO){
        bookDao.insert(book)
    }

    suspend fun getBook(): List<CacheBook>
    = withContext(Dispatchers.IO){
        val deferred = async {
            bookDao.getBook()
        }
        deferred.await()
    }
}