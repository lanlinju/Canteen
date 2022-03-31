package com.example.canteen.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.adapters.ChatAdapter
import com.example.canteen.databinding.FragmentChatBinding
import com.example.canteen.models.Chat
import com.example.canteen.models.Conversation
import com.example.canteen.models.User
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.ChatViewModel
import com.example.canteen.viewmodels.ConversationViewModel
import com.google.gson.Gson
import java.util.*


class ChatFragment : Fragment(){
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var conversationViewModel: ConversationViewModel
    private lateinit var chatMessages: MutableList<Chat>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var preferenceManager: PreferenceManager
    private  var conversationId: String? = null
    private lateinit var receiverUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        preferenceManager = requireActivity().getPreferenceManager()
        conversationViewModel = ViewModelProvider(this)[ConversationViewModel::class.java]
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chat, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()//获取聊天记录数据
        loadReceiverDetails()
        setObservers()
        setListeners()
        preferenceManager.getString(Constants.KEY_USER_ID)?.let {
            chatViewModel.getMessagesById(it, receiverUser.id)
        }

    }

    private fun init() {
        chatMessages = ArrayList()
        chatAdapter = ChatAdapter(
            chatMessages,
            receiverUser.image.toBitmap(),
            preferenceManager.getString(Constants.KEY_USER_ID)!!
        )
        binding.chatRecyclerView.adapter = chatAdapter

    }

    private fun loadReceiverDetails() {
        receiverUser = arguments?.getParcelable<User>("KEY_USER") as User
        arguments?.getString("KEY_CONVERSATION_ID")?.let {
            conversationId = it
        }
        binding.textName.text = receiverUser.name
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {
        with(chatViewModel) {//获取聊天数据
            chatListLiveData.observe(viewLifecycleOwner) { list ->
                val it = list as MutableList
                it.sortBy { it.dateTime }
                chatMessages.addAll(it)
                chatAdapter.notifyDataSetChanged() //刷新数据
                binding.chatRecyclerView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                if (conversationId == null) { //获取会话Id 第一次加载数据时候，（1次）
                    checkForConversion()
                }
            }
        }//监听服务发来的消息
        (requireActivity() as MainActivity).socketService.chatMessage.observe(viewLifecycleOwner){
            chatMessages.add(it)
            chatAdapter.notifyItemRangeInserted(chatMessages.size, chatMessages.size) //插入新的的数据
            binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size - 1) //设置滚动到末尾的位置
        }
        with(conversationViewModel){//获取会话id
            conversationIdLive.observe(viewLifecycleOwner){
                conversationId = it
            }
        }

    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener { v -> requireActivity().onBackPressed() }
        binding.layoutSent.setOnClickListener { v -> sendMessage() }
    }

    private fun sendMessage() {
        val message = Chat(
            receiverId = receiverUser.id,
            senderId = requireActivity().getPreferenceManager().getString(Constants.KEY_USER_ID)!!,
            message = binding.inputMessage.text.toString(),
            dateTime = Date()
        )
        (requireActivity() as MainActivity).socketService.sendMessage(Gson().toJson(message))
        chatViewModel.insertChat(message)
        if(conversationId != null){//更新最新消息
            conversationViewModel.updateLastMessage(conversationId!!, binding.inputMessage.text.toString())
        }else{//添加一个新会话
            conversationViewModel.insertConversion(Conversation(
                lastMessage =  binding.inputMessage.text.toString(),
                sendId = preferenceManager.getString(Constants.KEY_USER_ID)!!,
                sendName = preferenceManager.getString(Constants.KEY_NAME)!!,
                sendImage = preferenceManager.getString(Constants.KEY_IMAGE)!!,
                receiverId = receiverUser.id,
                receiverName = receiverUser.name,
                receiverImage = receiverUser.image,
                dateTime = Date()
            ))
            checkForConversion()
        }
        binding.inputMessage.text = null
    }

    private fun checkForConversion() { //获取会话Id 两人一个会话
        if (chatMessages.size != 0) {
            conversationViewModel.getConversationId(//获取id
                receiverUser.id,
                preferenceManager.getString(Constants.KEY_USER_ID)!!
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).binding.smoothBottomBar.visibility = View.GONE
    }

    override fun onDestroy() {
        (requireActivity() as MainActivity).binding.smoothBottomBar.visibility = View.VISIBLE
        super.onDestroy()
    }
}