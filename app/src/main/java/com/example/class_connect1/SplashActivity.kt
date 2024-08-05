package com.example.class_connect1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for 3 seconds before moving to the SocialLoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SocialLoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3 seconds delay
    }
}
