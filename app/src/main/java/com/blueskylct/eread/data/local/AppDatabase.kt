package com.blueskylct.eread.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blueskylct.eread.domain.model.CacheBook
import kotlin.concurrent.Volatile

@Database(entities = [CacheBook::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun bookDao(): CacheBookDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        //双重校验锁
        fun getInstance(context: Context): AppDatabase{
            return instance?: synchronized(this){
                instance?: buildDatabase(context)
                    .also {
                        instance = it
                    }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, "EReadDatabase")
                .addCallback(object: RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        //TODO
                    }
                })
                .build()
        }
    }
}