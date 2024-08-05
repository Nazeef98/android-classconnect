package com.example.class_connect1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var courseCode: String
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: List<User>
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val menuIcon: ImageView = findViewById(R.id.menu_icon)

        // Retrieve userId, token, name, and courseCode from GlobalData
        userId = GlobalData.userId ?: ""
        token = GlobalData.token ?: ""
        name = GlobalData.userName ?: ""
        courseCode = GlobalData.courseCode ?: ""

        val greetingTextView: TextView = findViewById(R.id.greeting_text)
        greetingTextView.text = "Welcome, $name!"
        Log.d("UserListActivity", "User ID: $userId")
        Log.d("UserListActivity", "Token: $token")
        Log.d("UserListActivity", "Course Code: $courseCode")

        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            Log.d("UserListActivity", "Pull to refresh triggered")
            refreshData()
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve the list of users passed via the intent
        userList = intent.getParcelableArrayListExtra<User>("userList") ?: listOf()
        adapter = UserListAdapter(userList)
        recyclerView.adapter = adapter

        // Set up edit profile button
        val editProfileButton: Button = findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            navigateToEditProfile()
        }
    }

    private fun refreshData() {
        Log.d("UserListActivity", "Refreshing data for course code: $courseCode")
        fetchUsersForCourse(courseCode)
    }

    private fun fetchUsersForCourse(courseCode: String) {
        Log.d("UserListActivity", "Fetching users for course code: $courseCode")
        ApiClient.apiService.getUsersForCourse(courseCode).enqueue(object : Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    userList = response.body()!!.data
                    Log.d("UserListActivity", "Users fetched successfully: ${userList.size} users")
                    userList.forEach { user ->
                        Log.d("UserListActivity", "User: ${user.name}, Country: ${user.country}")
                    }
                    adapter.updateData(userList)
                    Toast.makeText(this@UserListActivity, "Data refreshed", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("UserListActivity", "Failed to refresh data: ${response.message()}")
                    Log.e("UserListActivity", "Response code: ${response.code()}")
                    Log.e("UserListActivity", "Response body: ${response.errorBody()?.string()}")
                    Toast.makeText(this@UserListActivity, "Failed to refresh data", Toast.LENGTH_SHORT).show()
                }
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e("UserListActivity", "Error fetching data: ${t.message}")
                Toast.makeText(this@UserListActivity, "Error refreshing data", Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
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
                    startActivity(intent)
                    true
                }
                R.id.menu_logout -> {
                    // Handle Logout
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

    private fun navigateToEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
        intent.putExtra("id", userId)
        intent.putExtra("token", token)
        startActivity(intent)
    }
}
