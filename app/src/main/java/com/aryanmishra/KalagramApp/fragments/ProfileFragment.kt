package com.aryanmishra.KalagramApp.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.aryanmishra.KalagramApp.Models.User
import com.aryanmishra.KalagramApp.SignUpActivity
import com.aryanmishra.KalagramApp.adapters.ViewPagerAdapter
import com.aryanmishra.KalagramApp.databinding.FragmentProfileBinding
import com.aryanmishra.KalagramApp.utils.USER_NODES
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private  lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

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

    //edit profile
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater, container, false)

        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, SignUpActivity::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)
            activity?.finish()
        }

        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment(),"My Post")
        viewPagerAdapter.addFragments(MyReelFragment(),"My Reels")
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


        return binding.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()
        //Taking current user data (photo, name and email)
            Firebase.firestore.collection(USER_NODES).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    val user:User = it.toObject<User>()!!
                    binding.name.text=user.name
                    binding.bio.text=user.email
                    //check if image is null or empty
                    if (!user.image.isNullOrEmpty()){
                        Picasso.get().load(user.image).into(binding.profileImage)
                    }

                }
    }
}