package com.svt.firebasefileuploadapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.svt.firebasefileuploadapp.model.FileEntity
import com.svt.firebasefileuploadapp.repository.FileEntityRepository
import com.svt.firebasefileuploadapp.roomdb.FileAppRoomDatabase
import kotlinx.coroutines.launch

class FileEntityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FileEntityRepository
    val fileEntityData: LiveData<List<FileEntity>>

    init {
        val fileEntityDao = FileAppRoomDatabase.getDatabase(application).fileEntityDao()
        repository = FileEntityRepository(fileEntityDao)
        fileEntityData = repository.allFileEntity
    }

    fun insert(fileEntity: FileEntity) = viewModelScope.launch {
        repository.insertFileEntity(fileEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAllFileEntity()
    }
}