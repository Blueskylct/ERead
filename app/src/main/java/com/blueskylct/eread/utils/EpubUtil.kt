package com.blueskylct.eread.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.data.repository.Repository
import com.blueskylct.eread.domain.model.CacheBook
import com.blueskylct.eread.ui.reading.ReadingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import kotlin.coroutines.coroutineContext

object EpubUtil {
    private val epubReader = EpubReader()

    /**
     * @author Blueskylct
     * @since 2025/3/31
     * 从Uri加载epub文件，并存入数据库
     */
    fun loadEpubFromUri(activity: Activity, uri: Uri){
        try {
            if (isEpub(uri))
            {
                val inputStream = activity.contentResolver.openInputStream(uri)
                val book = epubReader.readEpub(inputStream)
                book?.let{
                    runBlocking(Dispatchers.IO) {
                        Repository.getInstance().insertBook(convertBook(uri, book))
                    }
                    MyApplication.getInstance().setBook(it)
                    activity.startActivity(Intent(activity, ReadingActivity::class.java))
                }
                inputStream?.close()
            }
            else{
                Toast.makeText(MyApplication.getInstance(), "错误的文件类型", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            //TODO
        }
    }

    /**
     * @author Blueskylct
     * @since 2025/4/2
     * 判断是否为epub文件
     */
    private fun isEpub(uri: Uri) =
        uri.path.toString().contains(".epub", false)

    /**
     * @author Blueskylct
     * @since 2025/4/2
     * 将Book类型转换为CacheBook
     */
    private fun convertBook(uri: Uri, book: Book) =
        CacheBook(uri.toString(), book.title, book.metadata.toString(), 0 )
}