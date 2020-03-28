package com.svt.firebasefileuploadapp.repository

import androidx.lifecycle.LiveData
import com.svt.firebasefileuploadapp.dao.FileEntityDao
import com.svt.firebasefileuploadapp.model.FileEntity

class FileEntityRepository(private val fileEntityDao: FileEntityDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allFileEntity: LiveData<List<FileEntity>> = fileEntityDao.getAlphabetizedFileEntitys()

    suspend fun insertFileEntity(fileEntity: FileEntity) {
        fileEntityDao.insert(fileEntity)
    }

    suspend fun deleteAllFileEntity() {
        fileEntityDao.deleteAll()
    }
}