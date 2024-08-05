package com.example.class_connect1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var stateEditText: EditText
    private lateinit var zipEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button
    private lateinit var token: String
    private lateinit var user: String
    private var isNewProfile = true // Default to new profile
    private lateinit var courseCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        addressEditText = findViewById(R.id.addressEditText)
        cityEditText = findViewById(R.id.cityEditText)
        stateEditText = findViewById(R.id.stateEditText)
        zipEditText = findViewById(R.id.zipEditText)
        countryEditText = findViewById(R.id.countryEditText)
        emailEditText = findViewById(R.id.emailEditText)
        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.backButton)

        // Retrieve user and token from GlobalData
        user = GlobalData.userId ?: ""
        token = GlobalData.token ?: ""
        courseCode = GlobalData.courseCode ?: ""
        Log.d("EditProfileActivity", "User: $user")
        Log.d("EditProfileActivity", "Token: $token")
        Log.d("EditProfileActivity", "CourseCode: $courseCode")

        // Set up the back button click listener
        backButton.setOnClickListener {
            finish() // Close the activity
        }

        // Check if the profile exists and populate the form
        checkProfileExists()

        // Set up the save button click listener
        saveButton.setOnClickListener {
            handleSaveProfile()
        }
    }

    private fun checkProfileExists() {
        Log.d("EditProfileActivity", "Fetching profile for user: $user")

        ApiClient.apiService.getUserProfile("Bearer $token",user).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                Log.d("EditProfileActivity", "Profile fetch response: $response")

                if (response.isSuccessful && response.body()?.data != null) {
                    val userData = response.body()?.data!!
                    Log.d("EditProfileActivity", "Profile data received: $userData")

                    // Check if the received data is null or contains actual user data
                    if (userData.name.isNullOrEmpty() && userData.phone.isNullOrEmpty()) {
                        Log.d("EditProfileActivity", "No valid profile data found, setting as new profile.")
                        isNewProfile = true
                    } else {
                        isNewProfile = false
                        populateForm(userData)
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "No profile data found"
                    Log.e("EditProfileActivity", "Failed to fetch profile or no data: $errorMessage")
                    isNewProfile = true // No data found, set flag to create new profile
                    clearFormFields() // Clear form fields if necessary
                    Toast.makeText(
                        this@EditProfileActivity,
                        "No profile found. You can create a new one.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e("EditProfileActivity", "API call failed: ${t.message}", t)
                isNewProfile = true // Set to create new profile in case of failure
                Toast.makeText(
                    this@EditProfileActivity,
                    "Error fetching profile data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun clearFormFields() {
        nameEditText.text.clear()
        phoneEditText.text.clear()
        addressEditText.text.clear()
        cityEditText.text.clear()
        stateEditText.text.clear()
        zipEditText.text.clear()
        countryEditText.text.clear()
        emailEditText.text.clear()
    }

    private fun populateForm(user: User) {
        nameEditText.setText(user.name)
        phoneEditText.setText(user.phone)
        addressEditText.setText(user.address)
        cityEditText.setText(user.city)
        stateEditText.setText(user.state)
        zipEditText.setText(user.zip)
        countryEditText.setText(user.country)
        emailEditText.setText(user.email)
    }

    private fun handleSaveProfile() {
        val name = nameEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val address = addressEditText.text.toString()
        val city = cityEditText.text.toString()
        val state = stateEditText.text.toString()
        val zip = zipEditText.text.toString()
        val country = countryEditText.text.toString()
        val email = emailEditText.text.toString()

        Log.d("EditProfileActivity", "isNewProfile: $isNewProfile")

        if (isNewProfile) {
            createUserProfile(name, phone, address, city, state, zip, country, email, courseCode)
        } else {
            updateUserProfile(name, phone, address, city, state, zip, country, email, courseCode)
        }
    }

    private fun createUserProfile(
        name: String,
        phone: String,
        address: String,
        city: String,
        state: String,
        zip: String,
        country: String,
        email: String,
        courseCode: String
    ) {
        val createProfileRequest = CreateProfileRequest(
            user,
            name,
            phone,
            address,
            city,
            state,
            zip,
            country,
            email,
            courseCode
        )

        Log.d("EditProfileActivity", "Sending createProfileRequest: $createProfileRequest")

        ApiClient.apiService.createUserProfile("Bearer $token",createProfileRequest)
            .enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("EditProfileActivity", "Profile created successfully: ${response.body()}")
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Profile created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Close the activity
                    } else {
                        Log.e("EditProfileActivity", "Error creating profile: ${response.errorBody()?.string()}")
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Error creating profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Log.e("EditProfileActivity", "API call failed: ${t.message}", t)
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Error creating profile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun updateUserProfile(
        name: String,
        phone: String,
        address: String,
        city: String,
        state: String,
        zip: String,
        country: String,
        email: String,
        courseCode: String
    ) {
        val updateProfileRequest = UpdateProfileRequest(
            user,
            name,
            phone,
            address,
            city,
            state,
            zip,
            country,
            email,
            courseCode
        )

        Log.d("EditProfileActivity", "Sending updateProfileRequest: $updateProfileRequest")

        ApiClient.apiService.updateUserProfile("Bearer $token",updateProfileRequest)
            .enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("EditProfileActivity", "Profile updated successfully: ${response.body()}")
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Profile updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Close the activity
                    } else {
                        Log.e("EditProfileActivity", "Error updating profile: ${response.errorBody()?.string()}")
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Error updating profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Log.e("EditProfileActivity", "API call failed: ${t.message}", t)
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Error updating profile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
