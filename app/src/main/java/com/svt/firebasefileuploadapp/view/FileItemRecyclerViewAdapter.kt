package com.svt.firebasefileuploadapp.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.svt.firebasefileuploadapp.R
import com.svt.firebasefileuploadapp.model.FileItem
import com.svt.firebasefileuploadapp.util.AppUtil
import kotlinx.android.synthetic.main.fragment_file_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [FileItem] and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
class FileItemRecyclerViewAdapter(
    private val mValues: ArrayList<FileItem>
) : RecyclerView.Adapter<FileItemRecyclerViewAdapter.ViewHolder>() {

    init {
    }

    fun updateFileItem(newUsers: List<FileItem>) {
        mValues.clear()
        mValues.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_file_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        if (!item.name.contains(".")) {
            holder.mImgView.setImageResource(R.mipmap.baseline_folder_open_black_18)
        } else {
            holder.mNameView.text = item.name
            holder.mFileSizeView.text = item.fileSize
            holder.mDateView.text = item.modifiedDate
        }

        with(holder.mView) {
            tag = item
            setOnClickListener {
                val path: String = item.uri
                val bundle = Bundle()

                if (path.contains(".")) {
                    bundle.putParcelable(AppUtil.FILE_DATA, item)
                    Navigation.findNavController(it).navigate(R.id.details_dest, bundle)
                } else {
                    bundle.putString(AppUtil.FILE_PATH, path)
                    Navigation.findNavController(it).navigate(R.id.fileItem_dest, bundle)
                }
            }
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mImgView: ImageView = mView.imgId
        val mNameView: TextView = mView.name
        val mFileSizeView: TextView = mView.fileSize
        val mDateView: TextView = mView.modDate

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
