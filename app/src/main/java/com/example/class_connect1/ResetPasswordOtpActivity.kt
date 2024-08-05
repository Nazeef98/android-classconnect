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

class ResetPasswordOtpActivity : AppCompatActivity() {

    private lateinit var otpEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmNewPasswordEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var email: String
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_otp)

        // Retrieve the email from the intent
        email = intent.getStringExtra("email") ?: ""
        token = GlobalData.token ?: ""

        otpEditText = findViewById(R.id.otp)
        newPasswordEditText = findViewById(R.id.new_password)
        confirmNewPasswordEditText = findViewById(R.id.confirm_new_password)
        resetPasswordButton = findViewById(R.id.reset_password_button)

        resetPasswordButton.setOnClickListener {
            val otp = otpEditText.text.toString()
            val password = newPasswordEditText.text.toString()
            val confirmNewPassword = confirmNewPasswordEditText.text.toString()

            // Log the OTP entered by the user for debugging purposes
            Log.d("User OTP", "Entered OTP: $otp")

            if (otp.isNotEmpty() && password.isNotEmpty() && confirmNewPassword.isNotEmpty()) {
                if (password == confirmNewPassword) {
                    resetPassword(email, otp, password)
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetPassword(email: String, otp: String, password: String) {
        // Log the request data for debugging
        Log.d("ResetPasswordRequest", "Request: email=$email, otp=$otp, password=$password")

        val resetPasswordRequest = ResetPasswordRequest(email, otp, password)
        ApiClient.apiService.resetPassword("Bearer $token", resetPasswordRequest)
            .enqueue(object : Callback<ResetPasswordResponse> {
                override fun onResponse(
                    call: Call<ResetPasswordResponse>,
                    response: Response<ResetPasswordResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val resetPasswordResponse = response.body()!!

                        // Log the full success response
                        Log.d("ResetPasswordResponse", "Success response: ${response.body()}")

                        // Log the status and message from the response
                        Log.d(
                            "ResetPasswordResponse",
                            "Status: ${resetPasswordResponse.status}, Message: ${resetPasswordResponse.message}"
                        )

                        if (resetPasswordResponse.status == 200) {
                            Toast.makeText(
                                this@ResetPasswordOtpActivity,
                                "Password reset successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(
                                this@ResetPasswordOtpActivity,
                                SocialLoginActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e(
                                "Reset Password Error",
                                "Invalid OTP: ${resetPasswordResponse.message}"
                            )
                            Toast.makeText(
                                this@ResetPasswordOtpActivity,
                                resetPasswordResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("Reset Password Error", "Response error: $errorBody")
                        Toast.makeText(
                            this@ResetPasswordOtpActivity,
                            "Failed to reset password: $errorBody",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    Log.e("Reset Password Error", "Failure: ${t.message}")
                    Toast.makeText(
                        this@ResetPasswordOtpActivity,
                        "An error occurred: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}