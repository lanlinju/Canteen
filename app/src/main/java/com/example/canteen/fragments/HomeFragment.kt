package com.example.canteen.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.canteen.R
import com.example.canteen.activities.SignInActivity
import com.example.canteen.databinding.FragmentHomeBinding
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.PreferenceManager
import com.example.canteen.utilities.showDialog


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        loadUserDetails()

        setListeners()
    }

    private fun setListeners() {
        binding.imageSignOut.setOnClickListener {
            requireContext().showDialog(getString(R.string.text_message_logout)) {
                preferenceManager.clear()
                startActivity(Intent(requireContext(), SignInActivity::class.java))
                onDestroy()
            }
        }

    }

    private fun loadUserDetails() {
        preferenceManager.getString(Constants.KEY_IMAGE)?.let {
            val bytes = Base64.decode(it, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.imageProfile.setImageBitmap(bitmap)
        }


    }
}