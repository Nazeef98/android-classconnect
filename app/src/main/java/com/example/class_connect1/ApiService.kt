package com.example.class_connect1

import android.os.Parcel
import android.os.Parcelable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.Serializable

// Request data classes
data class ChangePasswordRequest(
    val id: String,
    val oldPassword: String,
    val password: String
)

data class LoginRequest(val email: String, val password: String)

data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val contactNumber: String
)

// Response data classes
data class LoginResponse(val status: Int, val message: String, val data: UserResponseData)
data class CourseListResponse(val status: Int, val message: String, val data: List<CourseResponse>)
data class UserListResponse(val status: Int, val message: String, val data: List<User>)

// Data models
data class UserResponseData(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val contactNumber: String,
    val token: String
)

data class CourseResponse(
    val id: String,
    val title: String,
    val description: String
) : Serializable

data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val password: String
)

data class ResetPasswordResponse(
    val status: Int,
    val message: String
)

data class SendOtpRequest(val email: String)

data class SendOtpResponse(val status: Int, val message: String)

// User data model with Parcelable implementation
//
//data class User(
//    val id: String,
//    val name: String,
//    val phone: String,
//    val address: String,
//    val city: String,
//    val state: String,
//    val zip: String,
//    val country: String,
//    val courseCode: String,
//    val user: String,
//    val createdAt: Long,
//    val updatedAt: Long
//) : Parcelable {
//    // Constructor that takes a Parcel and reconstructs the object
//    constructor(parcel: Parcel) : this(
//        parcel.readString() ?: "",   // id
//        parcel.readString() ?: "",   // name
//        parcel.readString() ?: "",   // phone
//        parcel.readString() ?: "",   // address
//        parcel.readString() ?: "",   // city
//        parcel.readString() ?: "",   // state
//        parcel.readString() ?: "",   // zip
//        parcel.readString() ?: "",   // country
//        parcel.readString() ?: "",   // courseCode
//        parcel.readString() ?: "",   // user
//        parcel.readLong(),           // createdAt
//        parcel.readLong()            // updatedAt
//    )
//
//    // Writes the object to a Parcel
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(id)
//        parcel.writeString(name)
//        parcel.writeString(phone)
//        parcel.writeString(address)
//        parcel.writeString(city)
//        parcel.writeString(state)
//        parcel.writeString(zip)
//        parcel.writeString(country)
//        parcel.writeString(courseCode)
//        parcel.writeString(user)
//        parcel.writeLong(createdAt)
//        parcel.writeLong(updatedAt)
//    }
//
//    // Describes the contents, generally returning 0
//    override fun describeContents(): Int = 0
//
//    // CREATOR field to generate instances of the Parcelable class from a Parcel
//    companion object CREATOR : Parcelable.Creator<User> {
//        override fun createFromParcel(parcel: Parcel): User {
//            return User(parcel)
//        }
//
//        override fun newArray(size: Int): Array<User?> {
//            return arrayOfNulls(size)
//        }
//
//    }
//}
data class User(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val city: String,
    val state: String,
    val zip: String,
    val country: String,
    val email: String,
    val courseCode: String,
    val user: String,
    val createdAt: Long,
    val updatedAt: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",   // id
        parcel.readString() ?: "",   // name
        parcel.readString() ?: "",   // phone
        parcel.readString() ?: "",   // address
        parcel.readString() ?: "",   // city
        parcel.readString() ?: "",   // state
        parcel.readString() ?: "",   // zip
        parcel.readString() ?: "",   // country
        parcel.readString() ?: "",   // email
        parcel.readString() ?: "",   // courseCode
        parcel.readString() ?: "",   // user
        parcel.readLong(),           // createdAt
        parcel.readLong()            // updatedAt
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(zip)
        parcel.writeString(country)
        parcel.writeString(email)
        parcel.writeString(courseCode)
        parcel.writeString(user)
        parcel.writeLong(createdAt)
        parcel.writeLong(updatedAt)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

data class CreateProfileRequest(
    val user: String,
    val name: String,
    val phone: String,
    val address: String,
    val city: String,
    val state: String,
    val zip: String,
    val country: String,
    val email: String,
    val courseCode: String
)

data class UpdateProfileRequest(
    val user: String,
    val name: String,
    val phone: String,
    val address: String,
    val city: String,
    val state: String,
    val zip: String,
    val country: String,
    val email: String,
    val courseCode: String
)

//data class CreateProfileRequest(
//    val user: String,
//    val name: String,
//    val phone: String,
//    val address: String,
//    val city: String,
//    val state: String,
//    val zip: String,
//    val country: String,
//    val courseCode: String
//)
//
//data class UpdateProfileRequest(
//    val user: String,
//    val name: String,
//    val phone: String,
//    val address: String,
//    val city: String,
//    val state: String,
//    val zip: String,
//    val country: String,
//    val courseCode: String
//
//)
data class UserProfileResponse(
    val status: Int,
    val message: String,
    val data: User?
)


// API service interface
interface ApiService {
    @POST("api/auth/signIn")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/auth/signUp")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<LoginResponse>

    @GET("api/course/list")
    fun getCourses(@Header("Authorization") token: String): Call<CourseListResponse>

    @POST("api/auth/resetPassword")
    fun sendOtp(@Header("Authorization") token: String,@Body sendOtpRequest: SendOtpRequest): Call<SendOtpResponse>

    @POST("api/auth/updatePassword")
    fun resetPassword(
        @Header("Authorization")token: String,
        @Body resetPasswordRequest: ResetPasswordRequest): Call<ResetPasswordResponse>

    @GET("api/course/getUsers")
    fun getUsersForCourse(
//        @Header("Authorization")token: String,
        @Query("courseCode") courseCode: String): Call<UserListResponse>

    @POST("api/user/changePassword")
    fun changePassword(
        @Header("Authorization")token: String,
        @Body changePasswordRequest: ChangePasswordRequest): Call<ResetPasswordResponse>

    // Get user profile
    @GET("api/user/getProfile")
    fun getUserProfile(
        @Header("Authorization")token: String,
        @Query("id") id: String): Call<UserProfileResponse>

    // Create user profile
    @POST("api/user/createPublicProfile")
    fun createUserProfile(
        @Header("Authorization")token: String,
        @Body createProfileRequest: CreateProfileRequest): Call<UserProfileResponse>


    // Update user profile
//    @POST("api/user/updatePublicProfile")
//    temporary
//    @POST("http://192.168.1.10:1337/api/user/updatePublicProfile")
//    fun updateUserProfile(@Header("Authorization")token: String,
//                          @Body updateProfileRequest: UpdateProfileRequest
//    ): Call<UserProfileResponse>

    @POST("api/user/updatePublicProfile")
    fun updateUserProfile(
        @Query("id") id: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): Call<UserProfileResponse>
}

