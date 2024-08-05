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

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var sendCodeButton: Button
    private lateinit var token : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)
        token = GlobalData.token ?: ""

        emailEditText = findViewById(R.id.email)
        sendCodeButton = findViewById(R.id.send_code_button)

        sendCodeButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isNotEmpty()) {
                sendOtp(email)
            } else {
                Toast.makeText(this, "Please enter your registered email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendOtp(email: String) {
        val sendOtpRequest = SendOtpRequest(email)
        ApiClient.apiService.sendOtp("Bearer $token",sendOtpRequest).enqueue(object : Callback<SendOtpResponse> {
            override fun onResponse(call: Call<SendOtpResponse>, response: Response<SendOtpResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val sendOtpResponse = response.body()!!
                    if (sendOtpResponse.status == 200) {
                        Toast.makeText(this@ResetPasswordActivity, "OTP sent successfully", Toast.LENGTH_SHORT).show()

                        // No OTP logging, since the server does not send it back
                        val intent = Intent(this@ResetPasswordActivity, ResetPasswordOtpActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@ResetPasswordActivity, sendOtpResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("OTP Error", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ResetPasswordActivity, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SendOtpResponse>, t: Throwable) {
                Log.e("OTP Error", "Failure: ${t.message}")
                Toast.makeText(this@ResetPasswordActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
