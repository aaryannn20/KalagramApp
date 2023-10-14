package com.aryanmishra.KalagramApp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aryanmishra.KalagramApp.Models.User
import com.aryanmishra.KalagramApp.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK
        binding.loginAccount.setOnClickListener {
            if(binding.email.editText?.text.toString().equals("") or
                binding.password.editText?.text.toString().equals("")){
                Toast.makeText(this@LoginActivity, "Please fill the details", Toast.LENGTH_SHORT).show()
            }else{
                var user = User(binding.email.editText?.text.toString(),
                    binding.password.editText?.text.toString())

                Firebase.auth.signInWithEmailAndPassword(user.email!! , user.password!!)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            startActivity(Intent(this@LoginActivity, HomePageActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this@LoginActivity, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
        binding.createAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }
    }
}