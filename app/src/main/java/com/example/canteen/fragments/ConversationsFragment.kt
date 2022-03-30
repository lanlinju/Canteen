package com.example.canteen.fragments


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
import com.example.canteen.databinding.FragmentConversationsBinding
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.getPreferenceManager
import com.example.canteen.utilities.showLog
import com.example.canteen.viewmodels.ConversationViewModel


class ConversationsFragment : Fragment() {

    private lateinit var binding: FragmentConversationsBinding
    private lateinit var conversationViewModel: ConversationViewModel
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
        conversationViewModel.apply {
            conversationLiveData.observe(viewLifecycleOwner) {
                it.toString().showLog()
            }
            getAllConversations()
        }
        setListeners()
        loadUserDetails()
    }

    private fun setListeners() {
        binding.fabNewChat.setOnClickListener {
            findNavController().navigate(R.id.usersFragment)
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

}