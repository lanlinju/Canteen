package com.example.canteen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen.R
import com.example.canteen.databinding.ItemContainerRecentConversionBinding
import com.example.canteen.listeners.ConversionListener
import com.example.canteen.models.Conversation
import com.example.canteen.models.User
import com.example.canteen.utilities.toBitmap

class RecentConversationAdapter(
    private val chatMessages: List<Conversation>,
    private val conversionListener: ConversionListener
) :RecyclerView.Adapter<RecentConversationAdapter.ConversionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionViewHolder {
        return  ConversionViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_container_recent_conversion,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ConversionViewHolder, position: Int) {
       holder.setData(chatMessages[position])
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    inner class ConversionViewHolder(itemContainerRecentConversionBinding: ItemContainerRecentConversionBinding) :
        RecyclerView.ViewHolder(itemContainerRecentConversionBinding.root) {
        var binding: ItemContainerRecentConversionBinding = itemContainerRecentConversionBinding
        fun setData(conversation: Conversation) {
            binding.imageProfile.setImageBitmap(conversation.conversionImage.toBitmap())
            binding.textName.text = conversation.conversionName
            binding.textRecentMessage.text = conversation.lastMessage
            binding.root.setOnClickListener {
                val user = User(
                    id = conversation.conversionId,
                    name = conversation.conversionName,
                    image = conversation.conversionImage
                )
                conversionListener.onConversionClicked(user)
            }
        }

    }


}