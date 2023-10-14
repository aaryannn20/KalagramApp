package com.aryanmishra.KalagramApp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        window.statusBarColor = Color.BLACK

        Handler(Looper.getMainLooper()).postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null)
                startActivity(Intent(this, SignUpActivity::class.java))
            else
                startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }, 3000)
    }
}