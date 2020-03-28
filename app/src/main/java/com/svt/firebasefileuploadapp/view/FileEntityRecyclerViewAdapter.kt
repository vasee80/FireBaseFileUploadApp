package com.svt.firebasefileuploadapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.svt.firebasefileuploadapp.R
import com.svt.firebasefileuploadapp.model.FileEntity
import com.svt.firebasefileuploadapp.util.AppUtil

class FileEntityRecyclerViewAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<FileEntityRecyclerViewAdapter.FileEntityHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var fileList = emptyList<FileEntity>() // Cached copy of words

    inner class FileEntityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImgView: ImageView = itemView.findViewById(R.id.imgId)
        val mNameView: TextView = itemView.findViewById(R.id.name)
        val mFileSizeView: TextView = itemView.findViewById(R.id.fileSize)
        val mDateView: TextView = itemView.findViewById(R.id.modDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileEntityHolder {
        val itemView = inflater.inflate(R.layout.fragment_file_item, parent, false)
        return FileEntityHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileEntityHolder, position: Int) {
        val current = fileList[position]

        if (!current.fileName.contains(".")) {
            holder.mImgView.setImageResource(R.mipmap.baseline_folder_open_black_18)
        } else {
            holder.mNameView.text = current.fileName
            holder.mFileSizeView.text = current.fileSize
            holder.mDateView.text = current.updateDate
        }

        with(holder.itemView) {
            tag = current
            setOnClickListener {
                val path: String = current.uri
                val bundle = android.os.Bundle()

                if (path.contains(".")) {
                    bundle.putParcelable(
                        AppUtil.FILE_DATA,
                        current
                    )
                    androidx.navigation.Navigation.findNavController(it)
                        .navigate(R.id.details_dest, bundle)
                } else {
                    bundle.putString(AppUtil.FILE_PATH, path)
                    androidx.navigation.Navigation.findNavController(it)
                        .navigate(R.id.fileItem_dest, bundle)
                }
            }
        }
    }

    internal fun setFileEntity(words: List<FileEntity>) {
        this.fileList = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = fileList.size
}