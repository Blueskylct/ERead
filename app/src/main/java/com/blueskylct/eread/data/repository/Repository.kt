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

    /**
     * @author Blueskylct
     * @since 2025/4/1
     * 新增书籍
     */
    suspend fun insertBook(book: CacheBook)
    = withContext(Dispatchers.IO){
        bookDao.insert(book)
    }

    /**
     * @author Blueskylct
     * @since 2025/4/1
     * 删除书籍信息
     */
    suspend fun deleteBook(book: CacheBook)
            = withContext(Dispatchers.IO){
        bookDao.delete(book)
    }

    /**
     * @author Blueskylct
     * @since 2025/4/1
     * 加载全部书籍信息
     */
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
     * @since 2025/4/5
     * 从私有存储中加载epub
     */
    suspend fun loadEpubFromPrivateStorage(fileName: String): InputStream
    = withContext(Dispatchers.IO){
        val file = File(MyApplication.getInstance().filesDir, fileName)
        file.inputStream() as InputStream
    }

    /**
     * @author Blueskylct
     * @since 2025/4/6
     * 从私有存储中加载封面
     */
    suspend fun loadEpubCoverImage(fileName: String)
    = withContext(Dispatchers.IO){
        val file = File(MyApplication.getInstance().filesDir, "$fileName.jpg")
        file.inputStream()
    }
}