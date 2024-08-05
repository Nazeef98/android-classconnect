package com.example.class_connect1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserListAdapter(private var userList: List<User>) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.user_name)
        val countryTextView: TextView = itemView.findViewById(R.id.user_country)
        val viewButton: Button = itemView.findViewById(R.id.view_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.nameTextView.text = currentUser.name
        holder.countryTextView.text = currentUser.country

        // Log the data being bound
        Log.d("UserListAdapter", "Binding data for user: ${currentUser.name}, Country: ${currentUser.country}")

        // Set an OnClickListener to navigate to StudentDetailsActivity
        holder.viewButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, StudentDetailsActivity::class.java)
            intent.putExtra("user", currentUser) // Pass the entire User object
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = userList.size

    // Method to update the data in the adapter
    fun updateData(newUserList: List<User>) {
        userList = newUserList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
