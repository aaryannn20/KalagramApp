package com.aryanmishra.KalagramApp.Post

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aryanmishra.KalagramApp.HomePageActivity
import com.aryanmishra.KalagramApp.Models.Post
import com.aryanmishra.KalagramApp.Models.User
import com.aryanmishra.KalagramApp.databinding.ActivityPostBinding
import com.aryanmishra.KalagramApp.utils.POST
import com.aryanmishra.KalagramApp.utils.POST_FOLDER
import com.aryanmishra.KalagramApp.utils.USER_NODES
import com.aryanmishra.KalagramApp.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }

    var imageUrl:String? = null
    //Adding a Launcher to add Image on sign up screen
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let {
            uploadImage(uri, POST_FOLDER){
                url->
                if(url != null){
                    binding.selectImage.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Used to add toolbar with back arrow
        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        //back arrow to return to the last page
        binding.materialToolbar.setNavigationOnClickListener{
            startActivity(Intent(this@PostActivity, HomePageActivity::class.java))
            finish()
        }

        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@PostActivity, HomePageActivity::class.java))
            finish()
        }

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODES).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                var user=it.toObject<User>()!!

                val post:Post = Post(
                    postUrl = imageUrl!!,
                    caption = binding.caption.editText?.text.toString(),
                    uid =Firebase.auth.currentUser!!.uid,
                    time=System.currentTimeMillis().toString()
                )

                //for RealTime Database
                Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post)
                        .addOnSuccessListener {
                            startActivity(Intent(this@PostActivity, HomePageActivity::class.java))
                            finish()
                        }

                }
            }

        }
    }
}