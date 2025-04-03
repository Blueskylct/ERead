package com.blueskylct.eread.utils

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.data.repository.Repository
import com.blueskylct.eread.domain.model.CacheBook
import com.blueskylct.eread.ui.home.HomeActivity
import com.blueskylct.eread.ui.reading.ReadingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader

object EpubUtil {
    private val epubReader = EpubReader()

    /**
     * @author Blueskylct
     * @since 2025/3/31
     * 从Uri加载epub文件，并存入数据库
     */
    fun loadEpubFromUri(activity: HomeActivity, uri: Uri){
        try {
            if (isEpub(uri))
            {
                val inputStream = activity.contentResolver.openInputStream(uri)
                val book = epubReader.readEpub(inputStream)
                book?.let{
                    val cacheBook = convertBook(uri, book)
                    val list = activity.viewModel.bookListLiveData.value as ArrayList<CacheBook>
                    if (checkBook(list, cacheBook))
                        runBlocking(Dispatchers.IO) {
                            Repository.getInstance().insertBook(cacheBook)
                            activity.viewModel.loadBook()
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
     * 检查是否有重复的书
     */
    private fun checkBook(list: ArrayList<CacheBook>, book: CacheBook): Boolean{
        for ( mBook in list){
            if(mBook.uri == book.uri)
                return false
        }
        return true
    }
}