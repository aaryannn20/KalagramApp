package com.aryanmishra.KalagramApp.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aryanmishra.KalagramApp.Models.User
import com.aryanmishra.KalagramApp.adapters.SearchAdapter
import com.aryanmishra.KalagramApp.databinding.FragmentSearchBinding
import com.aryanmishra.KalagramApp.utils.USER_NODES
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var userList=ArrayList<User>()

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
        binding=FragmentSearchBinding.inflate(inflater,container,false)

        binding.rv.layoutManager=LinearLayoutManager(requireContext())
        adapter= SearchAdapter(requireContext(), userList)
        binding.rv.adapter=adapter


        Firebase.firestore.collection(USER_NODES).get().addOnSuccessListener {

            var tempList=ArrayList<User>()
            userList.clear()
            for (i in it.documents){
                if(i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())){

                }else{
                    var user:User = i.toObject<User>()!!
                    tempList.add(user)
                }

            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        binding.searchButton.setOnClickListener {
            var text=binding.searchView.text.toString()

            Firebase.firestore.collection(USER_NODES).whereEqualTo("name", text).get().addOnSuccessListener {
                var tempList=ArrayList<User>()
                userList.clear()

                if(it.isEmpty) {

                }else{
                    for (i in it.documents){
                        if(i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())){

                        }else{
                            var user:User = i.toObject<User>()!!
                            tempList.add(user)
                        }

                    }

                    userList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                }

            }
        }

        return binding.root
    }

    companion object {

    }


}