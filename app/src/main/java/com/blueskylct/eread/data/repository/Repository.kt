package com.blueskylct.eread.data.repository

import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.data.local.AppDatabase
import com.blueskylct.eread.domain.model.CacheBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubWriter
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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

    suspend fun deleteBook(book: CacheBook)
            = withContext(Dispatchers.IO){
        bookDao.delete(book)
    }

    suspend fun getBook(): List<CacheBook>
    = withContext(Dispatchers.IO){
        val deferred = async {
            bookDao.getBook()
        }
        deferred.await()
    }

    /**
     * @author Blueskylct
     * @since 2225/4/5
     * 将公共存储的文件转存到私有存储中
     */
    fun copyFileToPrivateStorage(fileName: String, book: Book){
        val epubWriter = EpubWriter()
        val privateFile = File(MyApplication.getInstance().filesDir, fileName)
        FileOutputStream(privateFile).use {
                outputStream -> epubWriter.write(book,outputStream)
        }

        val coverImage = book.coverImage.data
        val imageFile = File(MyApplication.getInstance().filesDir, "$fileName.jpg")
        FileOutputStream(imageFile).use {
            outputStream -> outputStream.write(coverImage)
        }
    }

    /**
     * @author Blueskylct
     * @since 2225/4/5
     * 从私有存储中加载epub
     */
    suspend fun loadEpubFromPrivateStorage(fileName: String): InputStream
    = withContext(Dispatchers.IO){
        val file = File(MyApplication.getInstance().filesDir, fileName)
        file.inputStream() as InputStream
    }

    suspend fun loadEpubCoverImage(fileName: String)
    = withContext(Dispatchers.IO){
        val file = File(MyApplication.getInstance().filesDir, "$fileName.jpg")
        file.inputStream()
    }
}