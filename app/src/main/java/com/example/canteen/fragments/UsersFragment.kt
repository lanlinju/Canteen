package com.example.canteen.fragments

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.canteen.R
import com.example.canteen.activities.MainActivity

import com.example.canteen.adapters.UsersAdapter
import com.example.canteen.databinding.FragmentUsersBinding
import com.example.canteen.listeners.UserListener
import com.example.canteen.models.User

import com.example.canteen.utilities.showLog
import com.example.canteen.viewmodels.UserViewModel

class UserFragment : Fragment(),UserListener {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var userViewModel: UserViewModel
    private var usersAdapter = UsersAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).toggleBottomNavigationVisibility()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        setObservers()
        fetchUsersData()
    }

    private fun setObservers() {
        with(userViewModel) {
            progressBarVisibility.observe(viewLifecycleOwner) {
                binding.progressBar.visibility = it
            }
            userListLiveData.observe(viewLifecycleOwner) { users ->
                toggleProgressBarVisibility()
                users.toString().showLog()
                usersAdapter.submitList(users)
                binding.usersRecycleView.visibility = View.VISIBLE
            }
        }

    }

    private fun init() {
        binding.usersRecycleView.adapter = usersAdapter
        binding.imageBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

    private fun fetchUsersData() {
        userViewModel.getAllUsers()
    }

    override fun onDestroy() {
        (requireActivity() as MainActivity).toggleBottomNavigationVisibility()
        super.onDestroy()
    }

    override fun onUserClicked(user: User) {
       Bundle().apply {
            putParcelable("KEY_USER",user)
           findNavController().navigate(R.id.chatFragment,this)
       }
    }

}