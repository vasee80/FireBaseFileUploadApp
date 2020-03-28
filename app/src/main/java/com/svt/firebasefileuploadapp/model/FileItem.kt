package com.svt.firebasefileuploadapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FileItem(val name: String, val fileSize: String, val modifiedDate: String, val uri: String) : Parcelable