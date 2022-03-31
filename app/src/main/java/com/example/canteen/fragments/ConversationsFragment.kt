package com.example.canteen.fragments


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.canteen.R
import com.example.canteen.adapters.RecentConversationAdapter
import com.example.canteen.databinding.FragmentConversationsBinding
import com.example.canteen.listeners.ConversionListener
import com.example.canteen.models.Conversation
import com.example.canteen.models.User
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.PreferenceManager
import com.example.canteen.utilities.getPreferenceManager
import com.example.canteen.viewmodels.ConversationViewModel


class ConversationsFragment : Fragment(), ConversionListener {

    private lateinit var binding: FragmentConversationsBinding
    private lateinit var conversationViewModel: ConversationViewModel
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var conversions: MutableList<Conversation>
    private lateinit var recentConversationAdapter: RecentConversationAdapter
    private val conversionId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        conversationViewModel = ViewModelProvider(this)[ConversationViewModel::class.java]
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_conversations, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()
        setListeners()
        init()
        loadUserDetails()
        conversationViewModel.getAllConversations()
    }

    private fun init() {
        conversions = ArrayList()
        preferenceManager = requireActivity().getPreferenceManager()
        recentConversationAdapter = RecentConversationAdapter(conversions, this)
        binding.conversationRecycleView.adapter = recentConversationAdapter
    }

    private fun setListeners() {
        binding.fabNewChat.setOnClickListener {
            findNavController().navigate(R.id.usersFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setObservers() {
            conversationViewModel.conversationsLiveData.observe(viewLifecycleOwner) { conversationList ->
                conversions.clear()//清楚livedata 缓存的数据
                conversationList.forEach { conversation ->
                    val senderId: String = conversation.sendId
                    val receiverId: String = conversation.receiverId
                    val tempConversation = Conversation(
                        sendId = senderId,
                        receiverId = receiverId,
                        dateTime = conversation.dateTime,
                        lastMessage = conversation.lastMessage
                    )
                    if (preferenceManager.getString(Constants.KEY_USER_ID)
                            .equals(senderId)
                    ) {
                        tempConversation.conversionId = conversation.receiverId
                        tempConversation.conversionName = conversation.receiverName
                        tempConversation.conversionImage = conversation.receiverImage
                    } else { //获取对方信息 如果对面先发的信息 对方的ID 为 senderId
                        tempConversation.conversionId = conversation.sendId
                        tempConversation.conversionName = conversation.sendName
                        tempConversation.conversionImage = conversation.sendImage
                    }
                    conversions.add(tempConversation)
                }
                conversions.sortByDescending { it.dateTime }
                recentConversationAdapter.notifyDataSetChanged()
                binding.conversationRecycleView.smoothScrollToPosition(0)
                binding.conversationRecycleView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
    }

    private fun loadUserDetails() {
        requireActivity().getPreferenceManager().apply {
            getString(Constants.KEY_IMAGE)?.let {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.imageProfile.setImageBitmap(bitmap)
            }
            getString(Constants.KEY_NAME)?.let {
                binding.textName.text = it
            }

        }
    }

    override fun onConversionClicked(user: User) {
        Bundle().apply {
            putParcelable("KEY_USER", user)
            findNavController().navigate(R.id.chatFragment, this)
        }
    }

}