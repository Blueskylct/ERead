package com.blueskylct.eread.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.blueskylct.eread.domain.model.CacheBook

@Dao
interface CacheBookDao {
    @Insert
    suspend fun insert(book: CacheBook)

    @Update
    suspend fun update(book: CacheBook)

    @Delete
    suspend fun delete(title: String)

    @Query("Select * from book")
    suspend fun getBook():LiveData<ArrayList<CacheBook>>
}