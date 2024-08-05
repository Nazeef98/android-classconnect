package com.example.class_connect1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var contactNumberEditText: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        firstNameEditText = findViewById(R.id.first_name)
        lastNameEditText = findViewById(R.id.last_name)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm_password)
        contactNumberEditText = findViewById(R.id.contact_number)
        signupButton = findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val contactNumber = contactNumberEditText.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && contactNumber.isNotEmpty()) {
                if (password == confirmPassword) {
                    signUpUser(firstName, lastName, email, password, contactNumber)
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpUser(firstName: String, lastName: String, email: String, password: String, contactNumber: String) {
        val signUpRequest = SignUpRequest(firstName, lastName, email, password, contactNumber)
        ApiClient.apiService.signUp(signUpRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    if (loginResponse.status == 200) {
//                        val token = loginResponse.data.token
//                        val fullName = "${loginResponse.data.firstName} ${loginResponse.data.lastName}"
//                        fetchCourses(token, fullName)
                        Toast.makeText(this@SignupActivity, "Registration successful", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        Toast.makeText(this@SignupActivity, loginResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else if (response.code() == 400) {
                    showDuplicateEntryError()
                } else {
                    Toast.makeText(this@SignupActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCourses(token: String, fullName: String) {
        ApiClient.apiService.getCourses("Bearer $token").enqueue(object : Callback<CourseListResponse> {
            override fun onResponse(call: Call<CourseListResponse>, response: Response<CourseListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val courseListResponse = response.body()!!
                    if (courseListResponse.status == 200) {
                        val courses = courseListResponse.data
                        val intent = Intent(this@SignupActivity, HomeActivity::class.java)
                        intent.putExtra("courses", ArrayList(courses))
                        intent.putExtra("userName", fullName)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SignupActivity, courseListResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "Failed to fetch courses", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CourseListResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDuplicateEntryError() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registration Error")
        builder.setMessage("An account with this email already exists.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }
}
