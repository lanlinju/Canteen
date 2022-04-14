package com.example.canteen.fragments


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen.R
import com.example.canteen.adapters.RecentConversationAdapter
import com.example.canteen.databinding.FragmentConversationsBinding
import com.example.canteen.listeners.ConversionListener
import com.example.canteen.models.Conversation
import com.example.canteen.models.Goods
import com.example.canteen.models.User
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.ConversationViewModel
import com.google.android.material.snackbar.Snackbar


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
        loadUserDetails()//加载会话通过userId
        conversationViewModel.getConversationsByUserId(preferenceManager.getString(Constants.KEY_USER_ID)!!)
    }

    private fun init() {
        conversions = ArrayList()
        preferenceManager = requireActivity().getPreferenceManager()
        recentConversationAdapter = RecentConversationAdapter(conversions, this)
        binding.conversationRecycleView.adapter = recentConversationAdapter
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START ) {
            //拖拽排序的操作由于动作的和数据更新不同步，所以不能这样简单处理。拖拽速度快了就出错了。
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                conversationViewModel.deleteConversation(conversions[viewHolder.adapterPosition].id!!)//滑动删除
                conversions.removeAt(viewHolder.adapterPosition)
                recentConversationAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

            //在滑动的时候，画出浅灰色背景和垃圾桶图标，增强删除的视觉效果
            var icon = ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_delete_forever_black_24dp
            )
            var background: Drawable = ColorDrawable(Color.LTGRAY)
            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
                val iconLeft: Int
                val iconRight: Int
                val iconBottom: Int
                val backLeft: Int
                val backRight: Int
                val backTop: Int = itemView.top
                val backBottom: Int = itemView.bottom
                val iconTop: Int = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
                iconBottom = iconTop + icon!!.intrinsicHeight
                if (dX < 0) {//右到左滑动 dx < 0
                    backRight = itemView.right
                    backLeft = itemView.right + dX.toInt()
                    background.setBounds(backLeft, backTop, backRight, backBottom)
                    iconRight = itemView.right - iconMargin
                    iconLeft = iconRight - icon!!.intrinsicWidth
                    icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                } else {
                    background.setBounds(0, 0, 0, 0)
                    icon!!.setBounds(0, 0, 0, 0)
                }
                background.draw(c)
                icon!!.draw(c)
            }
        }).attachToRecyclerView(binding.conversationRecycleView)
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
                        id = conversation.id,
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

    override fun onConversionClicked(user: User,position:Int) {
        Bundle().apply {
            putParcelable("KEY_USER", user)
            putString("KEY_CONVERSATION_ID",conversions[position].id)
            findNavController().navigate(R.id.chatFragment, this)
        }
    }

}