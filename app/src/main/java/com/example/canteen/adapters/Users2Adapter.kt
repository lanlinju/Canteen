package com.example.canteen.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen.R
import com.example.canteen.databinding.ItemContainerUserBinding
import com.example.canteen.models.User

class Users2Adapter(private val users: List<User>) :
    RecyclerView.Adapter<Users2Adapter.UserViewHolder>() {

    private fun getUseImage(encodedImage: String): Bitmap? {
        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

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
        holder.bindUser(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class UserViewHolder(private val binding: ItemContainerUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUser(user: User) {
            binding.user = user
        }
    }
}