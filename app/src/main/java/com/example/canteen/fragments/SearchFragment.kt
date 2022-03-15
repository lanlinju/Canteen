package com.example.canteen.fragments

import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.databinding.FragmentHomeBinding
import com.example.canteen.databinding.FragmentSearchBinding
import com.example.canteen.utilities.notShow
import com.example.canteen.utilities.show
import com.example.canteen.utilities.showKeyboard

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as MainActivity).binding.smoothBottomBar.notShow()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.imageBack.setOnClickListener { requireActivity().onBackPressed() }
        binding.inputSearch.showKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).binding.smoothBottomBar.show()
    }

}