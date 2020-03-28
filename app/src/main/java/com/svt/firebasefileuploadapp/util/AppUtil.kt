package com.svt.firebasefileuploadapp.util

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.svt.firebasefileuploadapp.R

object AppUtil {

    const val FILE_DATA = "fileData"
    const val FILE_PATH = "filePath"

    init {

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showProgressDialog(context: Context?): AlertDialog? {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_progress_dialog)
        return builder.create()
    }
}