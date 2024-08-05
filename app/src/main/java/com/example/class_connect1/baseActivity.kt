package com.example.class_connect1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    protected fun clearUserToken() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("user_token") // Replace "user_token" with your actual token key
        editor.apply()
    }

    protected fun navigateToLogin() {
        val intent = Intent(this, SocialLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
