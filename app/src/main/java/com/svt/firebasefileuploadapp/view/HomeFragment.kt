package com.svt.firebasefileuploadapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.svt.firebasefileuploadapp.R

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.homeUploadBttn)
            .setOnClickListener {
                findNavController().navigate(R.id.fileUpload_dest)
            }

        view.findViewById<Button>(R.id.viewBttn)
            .setOnClickListener {
                findNavController().navigate(R.id.fileItem_dest)
            }
    }
}
