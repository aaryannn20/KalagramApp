package com.aryanmishra.KalagramApp.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.aryanmishra.KalagramApp.Models.Reel
import com.aryanmishra.KalagramApp.adapters.ReelAdapter
import com.aryanmishra.KalagramApp.databinding.FragmentReelBinding
import com.aryanmishra.KalagramApp.utils.REEL
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ReelFragment : Fragment() {
    private lateinit var binding : FragmentReelBinding
    lateinit var adapter:ReelAdapter
    var reelList = ArrayList<Reel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Get the window associated with the fragment's activity
            val window: Window? = activity?.window
            window?.statusBarColor = Color.BLACK

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReelBinding.inflate(inflater, container, false)
        adapter = ReelAdapter(requireContext(), reelList)
        binding.viewPager.adapter=adapter

        Firebase.firestore.collection(REEL).get().addOnSuccessListener {
            var tempList = ArrayList<Reel>()
            reelList.clear()
            for (i in it.documents){
                var reel=i.toObject<Reel>()!!
                tempList.add(reel)

            }
            reelList.addAll(tempList)
            reelList.reverse()
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}