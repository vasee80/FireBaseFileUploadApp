package com.svt.firebasefileuploadapp.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.svt.firebasefileuploadapp.R
import com.svt.firebasefileuploadapp.util.AppUtil
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class FileUploadFragment : Fragment() {

    val PICK_IMAGE_REQUEST = 234
    private var filePath: Uri? = null
    private var fileImgView: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_upload, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileImgView = view.findViewById(R.id.imageViewId)

        view.findViewById<Button>(R.id.selectFileId).setOnClickListener {
            showFileChooser()
        }

        view.findViewById<Button>(R.id.uploadFileId).setOnClickListener {
            uploadFile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, filePath)
                fileImgView?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST
        )
    }

    //this method will upload the file
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            val progressDialog: AlertDialog? =
                AppUtil.showProgressDialog(activity)
            progressDialog?.show()
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageRef: StorageReference = storage.reference
            Log.i("TAG", "File path " + filePath!!.path)

            var file = filePath;

            if(file!=null) {
                var uploadTask = storageRef.child("files/${file.lastPathSegment}").putFile(file)

                // Listen for state changes, errors, and completion of the upload.
                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    println("Upload is $progress% done")
                    progressDialog?.setMessage("Upload is $progress% done")
                }.addOnPausedListener {
                    Toast.makeText(context, "Upload is paused", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    progressDialog?.dismiss()
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }.addOnSuccessListener {
                    progressDialog?.dismiss()
                    Toast.makeText(context, "File Uploaded ", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "No file has been selected", Toast.LENGTH_LONG).show()
        }
    }
}
