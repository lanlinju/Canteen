package com.example.canteen.fragments

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.unit.Constraints
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen.R
import com.example.canteen.activities.MainActivity

import com.example.canteen.adapters.UsersAdapter
import com.example.canteen.databinding.FragmentUsersBinding
import com.example.canteen.listeners.UserListener
import com.example.canteen.models.User
import com.example.canteen.utilities.*

import com.example.canteen.viewmodels.UserViewModel

class UserFragment : Fragment(), UserListener {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var users: MutableList<User>
    private var usersAdapter = UsersAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
            userListLiveData.observe(viewLifecycleOwner) { userList ->
                toggleProgressBarVisibility()
                val users = userList.filter { //过滤本地账号
                    it.id != requireActivity().getPreferenceManager()
                        .getString(Constants.KEY_USER_ID)!!
                }
                this@UserFragment.users = users.toMutableList()
                usersAdapter.submitList(users)
                binding.usersRecycleView.visibility = View.VISIBLE
            }
        }

    }

    private fun init() {
        binding.usersRecycleView.adapter = usersAdapter
        binding.imageBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (requireContext().isAuthorized()) {//管理员权限才可以删除
                    requireContext().showDialog(getString(R.string.delete_user), onDismiss = {
                        usersAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    }) {
                         userViewModel.deleteUser(users[viewHolder.adapterPosition].id)
                        with(users) {
                            removeAt(viewHolder.adapterPosition)
                            usersAdapter.submitList(users)
                            usersAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                        }
                    }
                } else {
                    "无法删除，原因权限不足！".showToast()
                    usersAdapter.notifyItemChanged(viewHolder.adapterPosition)
                }
            }
        }).attachToRecyclerView(binding.usersRecycleView)
    }

    private fun fetchUsersData() {
        userViewModel.getAllUsers()
    }

    override fun onUserClicked(user: User) {
        Bundle().apply {
            putParcelable("KEY_USER", user)
            findNavController().navigate(R.id.chatFragment, this)
        }
    }

    override fun onUserImageClicked(id: String) {
        if (requireContext().isAuthorized()) {
            Bundle().apply {
                putString("KEY_USER_ID", id)
                findNavController().navigate(R.id.profileFragment, this)
            }
        }else{
            "无法查看该用户信息，原因权限不足！".showToast()
        }
    }

}