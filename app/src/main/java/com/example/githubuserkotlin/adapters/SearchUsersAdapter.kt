package com.example.githubuserkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserkotlin.R
import com.example.githubuserkotlin.entities.User
import com.example.githubuserkotlin.holders.UserHolder
import java.util.*

class SearchUsersAdapter : RecyclerView.Adapter<UserHolder>() {
    private var users: ArrayList<User>
    fun setUsers(users: ArrayList<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.onBind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    init {
        users = ArrayList()
    }
}