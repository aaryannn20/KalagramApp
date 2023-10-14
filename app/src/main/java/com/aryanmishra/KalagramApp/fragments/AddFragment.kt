package com.aryanmishra.KalagramApp.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.aryanmishra.KalagramApp.Post.PostActivity
import com.aryanmishra.KalagramApp.Post.ReelActivity
import com.aryanmishra.KalagramApp.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//BottomSheetDialogFragment is used to get a pop up (Bottom neviagtion)
class AddFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Get the window associated with the fragment's activity
            val window: Window? = activity?.window
            window?.statusBarColor = Color.TRANSPARENT
            val decorView = window?.decorView
            decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddBinding.inflate(inflater, container, false)

        binding.addPost.setOnClickListener{
            activity?.startActivity(Intent(requireContext(), PostActivity::class.java))
            activity?.finish()
        }
        binding.addReel.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), ReelActivity::class.java))
        }

        return binding.root
    }

    companion object {

    }
}