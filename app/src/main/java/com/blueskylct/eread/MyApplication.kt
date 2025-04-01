package com.blueskylct.eread

import android.app.Application
import com.blueskylct.eread.data.local.AppDatabase
import com.blueskylct.eread.data.repository.Repository
import nl.siegmann.epublib.domain.Book

class MyApplication: Application() {

    companion object{
        private lateinit var instance: MyApplication

        fun getInstance(): MyApplication{
            return instance
        }

        private lateinit var db : AppDatabase

        fun getDatabase(): AppDatabase{
            return db
        }

        private lateinit var repository: Repository

        fun getRepository(): Repository{
            return repository
        }
    }
    private lateinit var book :Book

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = AppDatabase.getInstance(this)
        repository = Repository.getInstance()
    }
    fun setBook(book: Book){
        this.book = book
    }
    fun getBook() = book
}