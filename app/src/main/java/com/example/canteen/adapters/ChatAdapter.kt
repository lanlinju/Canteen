package com.example.canteen.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen.R
import com.example.canteen.databinding.ItemContainerReceivedMessageBinding
import com.example.canteen.databinding.ItemContainerSentMessageBinding
import com.example.canteen.models.Chat

const val VIEW_TYPE_SENT = 1
const val VIEW_TYPE_RECEIVED = 2

class ChatAdapter(
    private val chatMessages: List<Chat>,
    private val receiverProfileImage: Bitmap,
    private val senderId: String,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            SentMessageViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_container_sent_message,
                    parent,
                    false
                )
            )
        } else {
            ReceivedMessageViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_container_received_message,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            (holder as SentMessageViewHolder).setData(chatMessages[position])
        } else {
            (holder as ReceivedMessageViewHolder).setData(
                chatMessages[position],
                receiverProfileImage
            )
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].senderId == senderId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    inner class SentMessageViewHolder(itemContainerSendMessageBinding: ItemContainerSentMessageBinding) :
        RecyclerView.ViewHolder(itemContainerSendMessageBinding.root) {
        private val binding: ItemContainerSentMessageBinding = itemContainerSendMessageBinding
        fun setData(chatMessage: Chat) {
            binding.textMessage.text = chatMessage.message
            binding.textDataTime.text = chatMessage.dateTime.toString()
        }

    }

    inner class ReceivedMessageViewHolder(itemContainerReceivedMessageBinding: ItemContainerReceivedMessageBinding) :
        RecyclerView.ViewHolder(itemContainerReceivedMessageBinding.root) {
        private val binding: ItemContainerReceivedMessageBinding =
            itemContainerReceivedMessageBinding

        fun setData(chatMessage: Chat, receiverProfileBitmap: Bitmap?) {
            binding.textMessage.text = chatMessage.message
            binding.textDataTime.text = chatMessage.dateTime.toString()
            binding.imageProfile.setImageBitmap(receiverProfileBitmap)
        }

    }


}