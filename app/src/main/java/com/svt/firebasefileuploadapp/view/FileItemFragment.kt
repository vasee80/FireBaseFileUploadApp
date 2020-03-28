package com.svt.firebasefileuploadapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.svt.firebasefileuploadapp.R
import com.svt.firebasefileuploadapp.model.FileItem
import com.svt.firebasefileuploadapp.util.AppUtil
import kotlinx.android.synthetic.main.fragment_file_item_list.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class FileItemFragment : Fragment() {

    private val fileAdapter = FileItemRecyclerViewAdapter(arrayListOf())

    private var fileList: ArrayList<FileItem> = arrayListOf()

    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filePath = if (arguments != null) {
            arguments!!.getString(AppUtil.FILE_PATH)!!
        }else{
            ""
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_file_item_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = fileAdapter
        }
        getMetaData()

    }

    private fun getMetaData(){
        fileList.clear()

        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.reference
        var dirRef: StorageReference = if (filePath.isEmpty()) {
            storageRef
        } else {
            storageRef.child(filePath)
        }

        dirRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                    val item = FileItem(
                        prefix.name,
                        prefix.name,
                        prefix.name,
                        prefix.path
                    )
                    fileList.add(item)
                }

                listResult.items.forEach { item ->
                    // All the items under listRef.
                    getMetaData(item)
                }

                fileAdapter.updateFileItem(fileList)
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
            }
    }

    private fun getMetaData(reference: StorageReference) {
        reference.metadata.addOnSuccessListener { data ->
            val size = data.sizeBytes
            val fileSizeInKB = size / 1024
            val fileSizeInMB = fileSizeInKB / 1024
            var fileSizeStr = ""
            fileSizeStr = if (fileSizeInMB > 1) {
                fileSizeInMB.toString() + "MB"
            } else if (fileSizeInKB > 1) {
                fileSizeInKB.toString() + "KB"
            } else {
                size.toString() + "B"
            }
            val lastUpdateDate = data.updatedTimeMillis
            val dateFormat =
                SimpleDateFormat("dd-MM-yyyy")
            val date = Date(lastUpdateDate)
            val itemFile = data.name?.let {
                FileItem(
                    it,
                    fileSizeStr,
                    dateFormat.format(date),
                    data.path
                )
            }
            if (itemFile != null) {
                fileList.add(itemFile)
            }

            fileAdapter.updateFileItem(fileList)
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }
    }
}
