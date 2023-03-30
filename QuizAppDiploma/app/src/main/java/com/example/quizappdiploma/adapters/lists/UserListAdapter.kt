package com.example.quizappdiploma.adapters.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.users.UserModel

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>()
{
    var userData : List<UserModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value){
            field = value
            notifyDataSetChanged()
        }
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder
    {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_userlist, parent, false))
    }

    override fun getItemCount(): Int
    {
        return userData.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int)
    {
        val currentItem = userData[position]
        holder.itemView.findViewById<TextView>(R.id.userEmailView).text = currentItem.email.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(user: List<UserModel>)
    {
        this.userData = user
        notifyDataSetChanged()
    }
}