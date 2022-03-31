package com.example.canteen.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.canteen.R
import com.example.canteen.databinding.ActivityMainBinding
import com.example.canteen.utilities.*

class MainActivity : AppCompatActivity(), AnalyticsDelegate by AnalyticsDelegateImpl() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var socketService: JWebSocketClientService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadUserDetails()

        registerAnalytics(lifecycle)

        navController = findNavController(R.id.navHostFragment)
        binding.navigationView.itemIconTintList = null

        NavigationUI.setupWithNavController(binding.navigationView, navController)
        setupSmoothBottomMenu()

        setListeners()

    }

    override fun onStart() {
        super.onStart()
        val bindIntent = Intent(this, JWebSocketClientService::class.java)
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                socketService =
                    (service as JWebSocketClientService.JWebSocketClientBinder).service
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }
        startService(bindIntent)//启动服务
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun setListeners() {
        binding.smoothBottomBar.onItemSelected = {
//            displayToast("Item $it selected")
            //webSocketClientService.sendMsg("测试")
        }
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun init() {
    }

    fun toggleBottomNavigationVisibility() {
        with(binding.smoothBottomBar) {
            visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_bottom_navigation)
        val menu = popupMenu.menu
        binding.smoothBottomBar.setupWithNavController(menu, navController)
    }

    private fun loadUserDetails() {
        getPreferenceManager().apply {
            getString(Constants.KEY_IMAGE)?.let {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.imageProfile)
                    .setImageBitmap(bitmap)
            }
            getString(Constants.KEY_NAME)?.let {
                binding.navigationView.getHeaderView(0)
                    .findViewById<TextView>(R.id.textNickName).text = it
            }

        }
    }

    override fun onDestroy() {
        socketService.onDestroy()
        super.onDestroy()
    }
}