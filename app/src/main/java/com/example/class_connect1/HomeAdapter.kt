package com.example.class_connect1

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeAdapter(
    private val courseList: List<CourseResponse>,
    private val context: Context,
    private val userId: String,
    private val token: String
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
        val itemSubtitle: TextView = itemView.findViewById(R.id.item_subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = courseList[position]
        holder.itemImage.setImageResource(R.drawable.course_icon) // Assuming the same icon for all items
        holder.itemTitle.text = currentItem.title
        holder.itemSubtitle.text = currentItem.description

        holder.itemView.setOnClickListener {
            showDetailPopup(currentItem)
        }
    }

    override fun getItemCount() = courseList.size

    private fun showDetailPopup(course: CourseResponse) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.popup_course_detail, null)

        val popupImage: ImageView = view.findViewById(R.id.popup_image)
        val popupTitle: TextView = view.findViewById(R.id.popup_title)
        val popupDescription: TextView = view.findViewById(R.id.popup_description)
        val courseCodeInput: EditText = view.findViewById(R.id.course_code_input)

        val submitButton: Button = view.findViewById(R.id.submit_button)
        val closeButton: ImageButton = view.findViewById(R.id.close_button)

        popupImage.setImageResource(R.drawable.course_icon) // Assuming the same icon for all items
        popupTitle.text = course.title
        popupDescription.text = course.description

        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        submitButton.setOnClickListener {
            val courseCode = courseCodeInput.text.toString()
            GlobalData.courseCode = courseCode
            if (courseCode.isNotEmpty()) {
                fetchUsersForCourse(courseCode, dialog)
            } else {
                Toast.makeText(context, "Please enter a course code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUsersForCourse(courseCode: String, dialog: AlertDialog) {
        ApiClient.apiService.getUsersForCourse(courseCode).enqueue(object : Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val userList = response.body()!!.data

                    // Log the response data
                    Log.d("HomeAdapter", "Fetched users: ${userList.size} users found.")
                    userList.forEach { user ->
                        Log.d("HomeAdapter", "User: ${user.name}, Country: ${user.country}")
                    }

                    navigateToUserListActivity(userList)
                    dialog.dismiss()
                } else {
                    Log.e("HomeAdapter", "Failed to fetch users: ${response.message()}")
                    Toast.makeText(context, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e("HomeAdapter", "Error: ${t.message}")
                Toast.makeText(context, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToUserListActivity(userList: List<User>) {
        val intent = Intent(context, UserListActivity::class.java)
        intent.putParcelableArrayListExtra("userList", ArrayList(userList))
        intent.putExtra("id", userId)
        intent.putExtra("token", token)
        context.startActivity(intent)
    }
}
