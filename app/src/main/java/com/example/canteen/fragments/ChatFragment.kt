package com.example.canteen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.canteen.R
import com.example.canteen.databinding.FragmentChatBinding
import com.example.canteen.databinding.FragmentChatBindingImpl
import com.example.canteen.models.User
import com.example.canteen.utilities.BottomNavigationVisibilityDelegate
import com.example.canteen.utilities.BottomNavigationVisibilityImpl
import com.example.canteen.utilities.showToast
import com.example.canteen.viewmodels.ChatViewModel


class ChatFragment : Fragment(), BottomNavigationVisibilityDelegate by BottomNavigationVisibilityImpl() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatViewModel: ChatViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.getParcelable<User>("KEY_USER")
    }
}