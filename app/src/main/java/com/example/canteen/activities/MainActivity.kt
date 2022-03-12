package com.example.canteen.activities

import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.canteen.R
import com.example.canteen.databinding.ActivityMainBinding
import com.example.canteen.utilities.displayToast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)

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

}