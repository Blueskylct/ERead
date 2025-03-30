package com.blueskylct.eread.utils

import android.app.Activity
import android.net.Uri
import nl.siegmann.epublib.epub.EpubReader

object EpubUtil {
    private val epubReader = EpubReader()

    fun loadEpubFromUri(activity: Activity, uri: Uri){
        try {
            val inputStream = activity.contentResolver.openInputStream(uri)
            val book = epubReader.readEpub(inputStream)
        }catch (e: Exception){
            //TODO
        }
    }
}