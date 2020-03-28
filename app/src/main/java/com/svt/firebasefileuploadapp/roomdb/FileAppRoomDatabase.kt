package com.svt.firebasefileuploadapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.svt.firebasefileuploadapp.dao.FileEntityDao
import com.svt.firebasefileuploadapp.model.FileEntity

@Database(entities = arrayOf(FileEntity::class), version = 1, exportSchema = false)
abstract class FileAppRoomDatabase : RoomDatabase() {

    abstract fun fileEntityDao(): FileEntityDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.

        @Volatile
        private var INSTANCE: FileAppRoomDatabase? = null

        fun getDatabase(context: Context): FileAppRoomDatabase {
            val temInstance = INSTANCE
            if (temInstance != null) {
                return temInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FileAppRoomDatabase::class.java,
                    "fileapp_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}