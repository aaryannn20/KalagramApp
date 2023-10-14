package com.aryanmishra.KalagramApp.Post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aryanmishra.KalagramApp.HomePageActivity
import com.aryanmishra.KalagramApp.Models.Reel
import com.aryanmishra.KalagramApp.Models.User
import com.aryanmishra.KalagramApp.databinding.ActivityReelBinding
import com.aryanmishra.KalagramApp.utils.REEL
import com.aryanmishra.KalagramApp.utils.REEL_FOLDER
import com.aryanmishra.KalagramApp.utils.USER_NODES
import com.aryanmishra.KalagramApp.utils.uploadVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ReelActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityReelBinding.inflate(layoutInflater)
    }
    private lateinit var videoUrl:String
    lateinit var progressDialog:ProgressDialog
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let {
            uploadVideo(uri, REEL_FOLDER, progressDialog){
                    url->
                if(url != null){

                    videoUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog=ProgressDialog(this)

        //Used to add toolbar with back arrow
        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        //back arrow to return to the last page
        binding.materialToolbar.setNavigationOnClickListener{
            startActivity(Intent(this@ReelActivity, HomePageActivity::class.java))
            finish()
        }

        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@ReelActivity, HomePageActivity::class.java))
            finish()
        }

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODES).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                var user:User=it.toObject<User>()!!

                val reel: Reel = Reel(videoUrl!!, binding.caption.editText?.text.toString(), user.image!!)

                //for RealTime Database
                Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ REEL).document().set(reel)
                        .addOnSuccessListener {
                            startActivity(Intent(this@ReelActivity, HomePageActivity::class.java))
                            finish()
                        }

                }
            }

        }
    }
}