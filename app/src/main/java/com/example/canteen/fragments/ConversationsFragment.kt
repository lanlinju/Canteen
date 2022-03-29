package com.example.canteen.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.canteen.R
import com.example.canteen.databinding.FragmentConversationsBinding
import com.example.canteen.network.im.JWebSocketClient
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.getPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import kotlin.coroutines.CoroutineContext


class ConversationsFragment : Fragment() {

    private lateinit var binding: FragmentConversationsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_conversations,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}