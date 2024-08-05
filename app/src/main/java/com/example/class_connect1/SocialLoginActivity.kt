package com.example.class_connect1

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

class SocialLoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var forgotPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)

        // Initialize UI components
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        signUpButton = findViewById(R.id.sign_up_button)
        forgotPasswordButton = findViewById(R.id.forgot_password_button)

        // Set up the login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the sign up button click listener
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // Set up the forgot password button click listener
        forgotPasswordButton.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        ApiClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    if (loginResponse.status == 200) {
                        val token = loginResponse.data.token
                        val id = loginResponse.data.id
                        val fullName = "${loginResponse.data.firstName} ${loginResponse.data.lastName}"
                        val firstName = loginResponse.data.firstName
                        // Store globally
                        GlobalData.userId = id
                        GlobalData.token = token
                        GlobalData.userName = firstName

                        fetchCourses(token, fullName, id)
                    } else {
                        Toast.makeText(this@SocialLoginActivity, loginResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SocialLoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@SocialLoginActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCourses(token: String, fullName: String, id: String) {
        ApiClient.apiService.getCourses("Bearer $token").enqueue(object : Callback<CourseListResponse> {
            override fun onResponse(call: Call<CourseListResponse>, response: Response<CourseListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val courseListResponse = response.body()!!
                    if (courseListResponse.status == 200) {
                        val courses = courseListResponse.data
                        val intent = Intent(this@SocialLoginActivity, HomeActivity::class.java)
                        intent.putExtra("courses", ArrayList(courses))
                        intent.putExtra("userName", fullName)
                        intent.putExtra("id", id)
                        intent.putExtra("token", token)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SocialLoginActivity, courseListResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SocialLoginActivity, "Failed to fetch courses", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CourseListResponse>, t: Throwable) {
                Toast.makeText(this@SocialLoginActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
//              println("login issue,:${t.message}")
                Log.e("sociallogin","login failed",t)
            }

        })
    }
}
