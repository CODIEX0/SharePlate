package com.example.shareplate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareplate.R
import com.example.shareplate.data.User
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

class UserAdapter(
    private val users: List<User>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(user: User)
        fun onUpdateClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val usernameTextView: TextView = itemView.findViewById(R.id.name)
        private val userStatusTextView: TextView = itemView.findViewById(R.id.status)
        private val userImageView: ImageView = itemView.findViewById(R.id.img)
        private val updateButton: Button = itemView.findViewById(R.id.btnUpdate) // Add an Update button in the item layout

        init {
            itemView.setOnClickListener(this)
            updateButton.setOnClickListener {
                listener.onUpdateClick(users[adapterPosition])
            }
        }

        fun bind(user: User) {
            usernameTextView.text = user.username
            userStatusTextView.text = user.status

            // Load user image with Picasso
            Picasso.get()
                .load(user.userImage)
                .placeholder(R.mipmap.person)
                .error(R.mipmap.profile)
                .into(userImageView)

            // Show the Update button only if the user type is "broker"
            updateButton.visibility = if (user.userType == "broker") View.VISIBLE else View.GONE
        }

        override fun onClick(v: View?) {
            listener.onItemClick(users[adapterPosition])
        }
    }
}
