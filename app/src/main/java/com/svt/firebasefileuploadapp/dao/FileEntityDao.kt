package com.svt.firebasefileuploadapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.svt.firebasefileuploadapp.model.FileEntity

@Dao
interface FileEntityDao {
    @Query("SELECT * from file_table ORDER BY file_name ASC")
    fun getAlphabetizedFileEntitys(): LiveData<List<FileEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(file: FileEntity)

    @Query("DELETE FROM file_table")
    suspend fun deleteAll()
}