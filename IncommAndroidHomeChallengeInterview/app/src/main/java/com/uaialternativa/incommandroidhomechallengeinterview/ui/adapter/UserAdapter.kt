package com.uaialternativa.incommandroidhomechallengeinterview.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uaialternativa.incommandroidhomechallengeinterview.databinding.ItemUserBinding
import com.uaialternativa.incommandroidhomechallengeinterview.domain.model.User

/**
 * PRESENTATION LAYER
 *
 * Adapter: Responsible for converting data (User) into Views for the RecyclerView.
 * Uses ViewBinding to inflate the layout efficiently and safely.
 */
class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<User>()

    fun setUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvName.text = "${user.firstName} ${user.lastName}"
            binding.tvEmail.text = user.email
            binding.tvRole.text = "${user.role} • ${user.department}"

            // Glide loads the image from URL asynchronously
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.ivAvatar)
        }
    }
}
