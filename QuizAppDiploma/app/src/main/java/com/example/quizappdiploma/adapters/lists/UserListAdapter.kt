package com.example.quizappdiploma.adapters.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.users.UserModel

class UserListAdapter(
    private val onEdit: (UserModel) -> Unit,
    private val onDelete: (UserModel) -> Unit
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    var userData: List<UserModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val initial: TextView = itemView.findViewById(R.id.userInitial)
        val username: TextView = itemView.findViewById(R.id.usernameView)
        val email: TextView = itemView.findViewById(R.id.userEmailView)
        val roleBadge: TextView = itemView.findViewById(R.id.roleBadge)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_userlist, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount() = userData.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userData[position]
        val name = user.username ?: "?"
        holder.initial.text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
        holder.username.text = name
        holder.email.text = user.email ?: ""

        val role = when {
            user.isAdmin == 1 -> "Admin"
            user.isLecturer == 1 -> "Lecturer"
            else -> "Student"
        }
        holder.roleBadge.text = role

        holder.editBtn.setOnClickListener { onEdit(user) }
        holder.deleteBtn.setOnClickListener { onDelete(user) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(users: List<UserModel>) {
        userData = users
    }
}
