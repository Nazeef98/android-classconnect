//package com.example.class_connect1
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class ItemUserActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: UserAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_item_user)
//
//        recyclerView = findViewById(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        val courseCode = intent.getStringExtra("courseCode") ?: ""
//        fetchUsers(courseCode)
//    }
//
//    private fun fetchUsers(courseCode: String) {
//        ApiClient.apiService.getUsersByCourseCode(courseCode).enqueue(object : Callback<UserListResponse> {
//            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
//                if (response.isSuccessful && response.body() != null) {
//                    val userListResponse = response.body()!!
//                    if (userListResponse.status == 200) {
//                        adapter = UserAdapter(userListResponse.data)
//                        recyclerView.adapter = adapter
//                    } else {
//                        Toast.makeText(this@ItemUserActivity, userListResponse.message, Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Log.e("ItemUserActivity", "Error: ${response.errorBody()?.string()}")
//                    Toast.makeText(this@ItemUserActivity, "Failed to fetch users", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
//                Log.e("ItemUserActivity", "Failure: ${t.message}")
//                Toast.makeText(this@ItemUserActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}
