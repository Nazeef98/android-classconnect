package com.example.class_connect1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : BaseActivity() {
    private lateinit var userId: String
    private lateinit var token: String
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Retrieve data from GlobalData
        userName = GlobalData.userName ?: ""
        userId = GlobalData.userId ?: ""
        token = GlobalData.token ?: ""

        println("User Name: $userName")
        println("ID: $userId")
        Log.d("HomeActivity", "Username received: $userName")
        Log.d("HomeActivity", "User ID received: $userId")

        // Set the greeting text
        val greetingTextView: TextView = findViewById(R.id.greeting_text)
        greetingTextView.text = "Welcome, $userName!"

        // Retrieve the courses data
        val courses = intent.getSerializableExtra("courses") as? List<CourseResponse>

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = courses?.let { HomeAdapter(it, this, userId, token) }

        val menuIcon: ImageView = findViewById(R.id.menu_icon)
        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_about -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_change_password -> {
                    val intent = Intent(this, ChangePasswordActivity::class.java)
                    intent.putExtra("id", userId)
                    intent.putExtra("token", token)
                    intent.putExtra("userName", userName)
                    Log.d("HomeActivityd", "User ID sent: $userId")
                    Log.d("HomeActivityd", "Token sent: $token")
                    startActivity(intent)
                    true
                }
                R.id.menu_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun logout() {
        clearUserToken()
        navigateToLogin()
    }
}
