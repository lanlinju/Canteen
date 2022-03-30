package com.example.canteen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen.R
import com.example.canteen.databinding.ItemContainerUserBinding

import com.example.canteen.models.User


class UsersAdapter :ListAdapter<User,UsersAdapter.UserViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemContainerUserBinding>(
            layoutInflater,
            R.layout.item_container_user,
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindUser(getItem(position))
    }

    inner class UserViewHolder(private val binding: ItemContainerUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUser(user: User) {
            binding.user = user
        }
    }

    object DiffCallback :DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

}