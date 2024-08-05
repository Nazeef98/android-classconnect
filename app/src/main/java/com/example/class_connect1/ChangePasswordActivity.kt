package com.example.class_connect1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var userId: String

    private lateinit var oldPassword: EditText
    private lateinit var newPassword: EditText
    private lateinit var confirmNewPassword: EditText
    private lateinit var changePasswordButton: Button
    private lateinit var userName: String
    private lateinit var token: String  // Assuming token is passed or available

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

//        import username and token userid from global data
        userId = GlobalData.userId ?: ""
        token = GlobalData.token ?: ""


//        // Retrieve the userId and userName from the intent
//        userName = intent.getStringExtra("userName") ?: ""
//        Log.d("ChangePasswordActivity", "UserName received: $userName")
//        userId = intent.getStringExtra("id") ?: ""
//        Log.d("ChangePasswordActivity", "UserId received: $userId")
//        token = intent.getStringExtra("token") ?: ""  // Retrieve token if needed for fetchCourses
//        Log.d("ChangePasswordActivity", "Token received: $token")

        oldPassword = findViewById(R.id.old_password)
        newPassword = findViewById(R.id.new_password)
        confirmNewPassword = findViewById(R.id.confirm_new_password)
        changePasswordButton = findViewById(R.id.change_password_button)

        changePasswordButton.setOnClickListener {
            val oldPass = oldPassword.text.toString()
            val newPass = newPassword.text.toString()
            val confirmPass = confirmNewPassword.text.toString()

            Log.d("ChangePasswordActivity", "Old Password: $oldPass")
            Log.d("ChangePasswordActivity", "New Password: $newPass")
            Log.d("ChangePasswordActivity", "Confirm New Password: $confirmPass")

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                showAlertDialog("Empty Fields", "All fields are required.")
            } else if (newPass != confirmPass) {
                showAlertDialog("Password Mismatch", "The new passwords do not match. Please try again.")
                Log.d("ChangePasswordActivity", "New passwords do not match")
            } else {
                changePassword(userId, oldPass, newPass)
            }
        }
    }

    private fun changePassword(userId: String, oldPassword: String, newPassword: String) {
        if (userId.isEmpty()) {
            Log.e("ChangePasswordActivity", "UserId is empty. Cannot proceed with password change.")
            showAlertDialog("Error", "User ID not found. Please try again.")
            return
        }

        val changePasswordRequest = ChangePasswordRequest(userId, oldPassword, newPassword)
        Log.d("ChangePasswordActivity", "ChangePasswordRequest: $changePasswordRequest")

        ApiClient.apiService.changePassword("Bearer $token",changePasswordRequest).enqueue(object : Callback<ResetPasswordResponse> {
            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val resetPasswordResponse = response.body()!!
                    Log.d("ChangePasswordActivity", "Response: $resetPasswordResponse")
                    if (resetPasswordResponse.status == 200) {
                        fetchCourses()  // Fetch updated data before navigating
                    } else {
                        showAlertDialog("Error", resetPasswordResponse.message)
                        Log.d("ChangePasswordActivity", "Change password failed: ${resetPasswordResponse.message}")
                    }
                } else {
                    showAlertDialog("Failure", "Password change failed. Please try again.")
                    Log.e("ChangePasswordActivity", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                showAlertDialog("Error", "An error occurred: ${t.message}")
                Log.e("ChangePasswordActivity", "Failure: ${t.message}")
            }
        })
    }

    private fun fetchCourses() {
        if (token.isEmpty()) {
            showAlertDialog("Error", "Token not found. Cannot fetch courses.")
            return
        }

        ApiClient.apiService.getCourses("Bearer $token").enqueue(object : Callback<CourseListResponse> {
            override fun onResponse(call: Call<CourseListResponse>, response: Response<CourseListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val courseListResponse = response.body()!!
                    if (courseListResponse.status == 200) {
                        val courses = courseListResponse.data
                        navigateToHome(courses)
                    } else {
                        showAlertDialog("Error", courseListResponse.message)
                        Log.d("ChangePasswordActivity", "Fetching courses failed: ${courseListResponse.message}")
                    }
                } else {
                    showAlertDialog("Failure", "Failed to fetch courses.")
                    Log.e("ChangePasswordActivity", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CourseListResponse>, t: Throwable) {
                showAlertDialog("Error", "An error occurred: ${t.message}")
                Log.e("ChangePasswordActivity", "Failure: ${t.message}")
            }
        })
    }

    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun navigateToHome(courses: List<CourseResponse>) {
//        val intent = Intent(this, HomeActivity::class.java)
//        intent.putExtra("courses", ArrayList(courses))
//        intent.putExtra("userName", userName)
//        Log.d("userName", "userName received: $userName")
//        intent.putExtra("id", userId)
//        startActivity(intent)
        finish()
    }
}
