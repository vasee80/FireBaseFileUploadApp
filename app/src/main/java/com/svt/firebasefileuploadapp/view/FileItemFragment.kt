package com.svt.firebasefileuploadapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.svt.firebasefileuploadapp.R
import com.svt.firebasefileuploadapp.model.FileEntity
import com.svt.firebasefileuploadapp.util.AppUtil
import com.svt.firebasefileuploadapp.viewmodel.FileEntityViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class FileItemFragment : Fragment() {

    private lateinit var filePath: String

    private lateinit var fileEntityViewModel: FileEntityViewModel

    private var fileId = 1

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

        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val adapter = FileEntityRecyclerViewAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fileEntityViewModel = ViewModelProvider(this).get(FileEntityViewModel::class.java)

        fileEntityViewModel.fileEntityData.observe(viewLifecycleOwner, Observer { file ->
            // Update the cached copy of the words in the adapter.
            file?.let { adapter.setFileEntity(it) }
        })

        getMetaData()

        return view
    }

    private fun getMetaData(){
        fileEntityViewModel.deleteAll()

        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.reference
        val dirRef: StorageReference = if (filePath.isEmpty()) {
            storageRef
        } else {
            storageRef.child(filePath)
        }

        dirRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.

                    val item = FileEntity(
                        fileId,
                        prefix.name,
                        prefix.name,
                        prefix.name,
                        prefix.path
                    )
                    fileEntityViewModel.insert(item)
                    fileId++
                }

                listResult.items.forEach { item ->
                    // All the items under listRef.
                    getMetaData(item)
                }
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
            val fileSizeStr = when {
                fileSizeInMB > 1 -> {
                    fileSizeInMB.toString() + "MB"
                }
                fileSizeInKB > 1 -> {
                    fileSizeInKB.toString() + "KB"
                }
                else -> {
                    size.toString() + "B"
                }
            }
            val lastUpdateDate = data.updatedTimeMillis
            val dateFormat =
                SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = Date(lastUpdateDate)

            val itemFile = data.name?.let {
                FileEntity(
                    fileId,
                    it,
                    fileSizeStr,
                    dateFormat.format(date),
                    data.path
                )
            }
            if (itemFile != null) {
                fileEntityViewModel.insert(itemFile)
                fileId++
            }

            //fileAdapter.updateFileItem(fileList)
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }
    }
}
