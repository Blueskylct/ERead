package com.blueskylct.eread.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.ui.reading.ReadingActivity
import nl.siegmann.epublib.epub.EpubReader

object EpubUtil {
    private val epubReader = EpubReader()

    fun loadEpubFromUri(activity: Activity, uri: Uri){
        try {
            val inputStream = activity.contentResolver.openInputStream(uri)
            val book = epubReader.readEpub(inputStream)
            book?.let {
                MyApplication.getInstance().setBook(it)
                activity.startActivity(Intent(activity, ReadingActivity::class.java))
            }
            inputStream?.close()
        }catch (e: Exception){
            //TODO
        }
    }
}