package com.example.canteen.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.canteen.R
import com.example.canteen.databinding.ActivityMainBinding
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.getPreferenceManager
import com.example.canteen.utilities.showLogD

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadUserDetails()

        navController = findNavController(R.id.navHostFragment)
        binding.navigationView.itemIconTintList = null

        NavigationUI.setupWithNavController(binding.navigationView, navController)

        //binding.navigationView.setItemBackgroundResource(R.color.mtrl_navigation_item_background_color)

        setListeners()
        setupSmoothBottomMenu()
    }

    private fun setListeners() {
//        binding.smoothBottomBar.onItemSelected = {
//            displayToast("Item $it selected")
//        }
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_bottom_navigation)
        val menu = popupMenu.menu
        binding.smoothBottomBar.setupWithNavController(menu, navController)
    }

    private fun init() {
    }

    private fun loadUserDetails() {
        getPreferenceManager().apply {
            getString(Constants.KEY_IMAGE)?.let {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.imageProfile).setImageBitmap(bitmap)
            }
            getString(Constants.KEY_NAME)?.let{
                binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.textNickName).text = it
            }

        }
    }

}