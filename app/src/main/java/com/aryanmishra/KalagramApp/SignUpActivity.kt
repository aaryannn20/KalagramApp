package com.aryanmishra.KalagramApp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aryanmishra.KalagramApp.Models.User
import com.aryanmishra.KalagramApp.databinding.ActivitySignUpBinding
import com.aryanmishra.KalagramApp.utils.USER_NODES
import com.aryanmishra.KalagramApp.utils.USER_PROFILE_FOLDER
import com.aryanmishra.KalagramApp.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class SignUpActivity : AppCompatActivity() {

    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    lateinit var user: User


    //Adding a Launcher to add Image on sign up screen
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER){
                if(it != null){
                    user.image = it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK
        val text = "<font color=#FF000000>Already have an account</font> <font color=#1E88E5>Login?</font>"
        binding.login.setText(Html.fromHtml(text))

        user = User()

        //edit profile functionality
        if (intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE", -1) == 1){

                binding.signUp.text = "Update Profile"
                Firebase.firestore.collection(USER_NODES).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {

                        user = it.toObject<User>()!!
                        if (!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(binding.profileImage)
                        }
                        binding.name.editText?.setText(user.name)
                        binding.email.editText?.setText(user.email)
                        binding.password.editText?.setText(user.password)

                    }
            }
        }

        binding.signUp.setOnClickListener {

            if (intent.hasExtra("MODE")){
                if (intent.getIntExtra("MODE", -1) == 1){
                    Firebase.firestore.collection(USER_NODES)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@SignUpActivity, HomePageActivity::class.java))
                            finish()
                        }
                }
            }
            else {

                if (binding.name.editText?.text.toString().equals("") or
                    binding.password.editText?.text.toString().equals("") or
                    binding.email.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please fill the details",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    //first method firebase
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()
                    ).addOnCompleteListener { result ->

                        if (result.isSuccessful) {
                            //second method firebase
                            user.name = binding.name.editText?.text.toString()
                            user.email = binding.email.editText?.text.toString()
                            user.password = binding.password.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODES)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            HomePageActivity::class.java
                                        )
                                    )
                                    finish()
                                }

                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }
    }
}