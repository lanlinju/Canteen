package com.example.canteen.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.activities.SignInActivity
import com.example.canteen.adapters.HomeViewPagerAdapter
import com.example.canteen.databinding.FragmentHomeBinding
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.PreferenceManager
import com.example.canteen.utilities.showDialog
import com.example.canteen.viewmodels.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.makeramen.roundedimageview.RoundedImageView


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        loadUserDetails()
        init()
        setListeners()
    }

    private fun setListeners() {
        requireActivity().findViewById<RoundedImageView>(R.id.imageProfile).setOnClickListener {
            (requireActivity() as MainActivity).binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.inputSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.imageSignOut.setOnClickListener {
            requireContext().showDialog(getString(R.string.text_message_logout)) {
                preferenceManager.clear()
                startActivity(Intent(requireContext(), SignInActivity::class.java))
                onDestroy()
            }
        }

    }

    fun init() {
        homeViewModel.categoryLiveData.observe(viewLifecycleOwner) { baseResponse ->
            if (baseResponse.code == 404 || baseResponse.code == -1) {
                requireActivity().showDialog(baseResponse.msg) {
                }
                return@observe
            }
            if (baseResponse.data != null) {
                val tabLayout = binding.tabLayout
                val viewPager = binding.viewPager2

                viewPager.adapter = HomeViewPagerAdapter(baseResponse.data!!, this)

                // Set the icon and text for each tab
                TabLayoutMediator(tabLayout, viewPager) { tab, position->
                    tab.text = baseResponse.data!![position].cname
                }.attach()

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