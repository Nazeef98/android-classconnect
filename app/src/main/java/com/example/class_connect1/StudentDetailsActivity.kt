package com.example.class_connect1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.class_connect1.utils.LocationUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class StudentDetailsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var user: User? = null
    private lateinit var name: String
    private lateinit var progressBar: ProgressBar

    private lateinit var nameTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var stateTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var zipTextView: TextView
    private lateinit var emailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_details_activity)

        name = GlobalData.userName ?: ""
        val greetingTextView: TextView = findViewById(R.id.greeting_text)
        greetingTextView.text = "Welcome, $name!"

        val menuIcon: ImageView = findViewById(R.id.menu_icon)
        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }

        // Initialize views
        progressBar = findViewById(R.id.progressBar)

        nameTextView = findViewById(R.id.nameTextView)
        phoneTextView = findViewById(R.id.phoneTextView)
        addressTextView = findViewById(R.id.addressTextView)
        cityTextView = findViewById(R.id.cityTextView)
        stateTextView = findViewById(R.id.stateTextView)
        countryTextView = findViewById(R.id.countryTextView)
        zipTextView = findViewById(R.id.zipTextView)
        emailTextView = findViewById(R.id.emailTextView)

        // Retrieve the User object from the Intent
        user = intent.getParcelableExtra("user")

        // Display the user details
        user?.let {
            nameTextView.text = it.name
            phoneTextView.text = it.phone
            addressTextView.text = it.address
            cityTextView.text = it.city
            stateTextView.text = it.state
            countryTextView.text = it.country
            zipTextView.text = it.zip
            emailTextView.text = it.email
        }

        // Initialize the map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Close the current activity and return to the previous one
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Show the user's location based on address and city
        user?.let {
            progressBar.visibility = View.VISIBLE
            val location = LocationUtils.getLocationFromAddress(this, it.address, it.city)
            progressBar.visibility = View.GONE

            location?.let { latLng ->
                mMap.addMarker(MarkerOptions().position(latLng).title(it.name))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } ?: run {
                // Handle the case where location is not found
                progressBar.visibility = View.GONE
                // Optionally, show a message to the user
            }
        }
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
}


















