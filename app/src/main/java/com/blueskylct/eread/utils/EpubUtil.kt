package com.blueskylct.eread.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.data.repository.Repository
import com.blueskylct.eread.domain.model.CacheBook
import com.blueskylct.eread.ui.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import java.io.InputStream

object EpubUtil {
    private val epubReader = EpubReader()

    /**
     * @author Blueskylct
     * @since 2025/4/5
     * 从私有存储加载epub文件
     */
    fun loadEpub(uri: Uri): Boolean{
        val inputStream : InputStream
        runBlocking(Dispatchers.IO) {
            val deferred = async {
                Repository.getInstance().loadEpubFromPrivateStorage(getFileNameFromUri(uri))
            }
            inputStream = deferred.await()
        }
        val book = epubReader.readEpub(inputStream)
        inputStream.close()
        book?.let {
            MyApplication.getInstance().setBook(it)
            return true
        }
        return false
    }

    /**
     * @author Blueskylct
     * @since 2025/3/31
     * 从Uri加载epub文件
     */
    fun loadEpubFromUri(activity: HomeActivity, uri: Uri): Boolean{
        try {
            if (isEpub(uri))
            {
                val inputStream = activity.contentResolver.openInputStream(uri)
                val book = epubReader.readEpub(inputStream)
                book?.let{
                    if (checkBook(activity.viewModel.bookListLiveData.value as ArrayList, convertBook(uri, book))) {
                        saveToRoom(activity, uri, book)
                        Repository.getInstance().copyFileToPrivateStorage(getFileNameFromUri(uri), book)
                    }
                    MyApplication.getInstance().setBook(it)
                    return true
                }
            }
            else{
                Toast.makeText(MyApplication.getInstance(), "错误的文件类型", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return false
    }

    /**
     * @author Blueskylct
     * @since 2025/4/7
     */
    fun loadEpubCoverImage(uri: Uri): Bitmap = runBlocking(Dispatchers.IO){
        val deferred = async {
            val inputStream = Repository.getInstance().loadEpubCoverImage(getFileNameFromUri(uri))
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        }
        deferred.await()
    }


    /**
     * @author Blueskylct
     * @since 2025/4/4
     * 将书籍信息存入数据库
     */
    private fun saveToRoom(activity: HomeActivity, uri: Uri, book: Book){
        val cacheBook = convertBook(uri, book)
        runBlocking(Dispatchers.IO) {
            launch {
                Repository.getInstance().insertBook(cacheBook)
                activity.viewModel.loadBook()
            }
        }
    }

    /**
     * @author Blueskylct
     * @since 2025/4/2
     * 判断是否为epub文件
     */
    private fun isEpub(uri: Uri) =
        uri.path.toString().contains(".epub", true)

    /**
     * @author Blueskylct
     * @since 2025/4/2
     * 将Book类型转换为CacheBook
     */
    private fun convertBook(uri: Uri, book: Book) =
        CacheBook(uri.toString(), book.title, book.metadata.toString(), 0 )

    /**
     * @author Blueskylct
     * @since 2025/4/3
     * 检查是否有重复的书，没有重复就返回true， 反则false
     */
    private fun checkBook(list: ArrayList<CacheBook>, book: CacheBook): Boolean{
        for ( mBook in list){
            if(mBook.uri == book.uri)
                return false
        }
        return true
    }

    /**
     * @author Blueskylct
     * @since 2025/4/5
     * 从Uri中获取epub文件名
     */
    fun getFileNameFromUri(uri: Uri) =
        Regex( "[^/]+\\.epub\$", RegexOption.IGNORE_CASE).find(uri.path.toString())?.value ?: "default.epub"

    /**
     * @author Blueskylct
     * @since 2025/4/11
     * 获取章节信息
     */
    fun getChapter(book: Book): ArrayList<String>{
        val list = ArrayList<String>()
        val contents = book.contents
        for(resource in contents){
            list.add(String(resource.data, Charsets.UTF_8))
        }
        return list
    }
}