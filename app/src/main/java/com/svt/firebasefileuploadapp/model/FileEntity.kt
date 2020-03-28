package com.svt.firebasefileuploadapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "file_table")
class FileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "file_name") val fileName: String,
    @ColumnInfo(name = "file_size") val fileSize: String,
    @ColumnInfo(name = "updated_date") val updateDate: String,
    @ColumnInfo(name = "file_uri") val uri: String
) : Parcelable