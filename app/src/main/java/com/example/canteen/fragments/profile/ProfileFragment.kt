package com.example.canteen.fragments.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.databinding.FragmentProfileBinding
import com.example.canteen.fragments.ProfileError
import com.example.canteen.fragments.ProfileScreen
import com.example.canteen.models.Goods
import com.example.canteen.theme.ui.CanteenM3Theme
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.UserViewModel


class ProfileFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java].apply {
            userLiveData.observe(viewLifecycleOwner) {
                it.toString().showLog()
            }
            if (!isLoaded) {
                isLoaded = true
                requireActivity().getPreferenceManager().getString(Constants.KEY_USER_ID)
                    ?.let { getUserById(it) }
            }

        }
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater, R.layout.fragment_profile, container, false
        ).apply {
            composeView.setContent {
                val userData by userViewModel.userLiveData.observeAsState()
                CanteenM3Theme {
                    userData?.let {
                        ProfileScreen(it)
                    }
                }
            }

        }
        return binding.root
    }

    override fun onDestroy() {
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onDestroy()
    }
}