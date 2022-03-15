package com.example.canteen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.utilities.notShow
import com.example.canteen.utilities.show
import com.example.canteen.utilities.showLogD


class GoodsDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showLogD("GoodsDetails:onCreateView:111")
        (requireActivity() as MainActivity).binding.smoothBottomBar.notShow()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goods_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.textGoodsDetails).setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }
    }
    override fun onResume() {
        super.onResume()
        showLogD("GoodsDetails:onResume:2222")
    }

    override fun onStop() {
        super.onStop()
        showLogD("GoodsDetails:onStop:3333")
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).binding.smoothBottomBar.show()
        showLogD("GoodsDetails:onDestroy:4444")
    }
}