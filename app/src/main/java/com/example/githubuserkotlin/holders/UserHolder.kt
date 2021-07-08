package com.example.githubuserkotlin.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserkotlin.R
import com.example.githubuserkotlin.entities.User

class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var nickname: TextView
    var fullname: TextView
    fun onBind(user: User) {
        nickname.text = user.nickname
        fullname.text = user.full_name
    }

    init {
        nickname = itemView.findViewById(R.id.nickfield)
        fullname = itemView.findViewById(R.id.fullnamefield)
    }
}