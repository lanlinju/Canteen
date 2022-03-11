package com.example.canteen.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.example.canteen.R
import com.example.canteen.databinding.ActivityMainBinding
import com.example.canteen.utilities.PreferenceManager
import com.example.canteen.utilities.displayToast
import com.example.canteen.utilities.showDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        preferenceManager = PreferenceManager(applicationContext)

        setListeners()
    }

    private fun init() {
    }

    private fun setListeners() {
        binding.imageSignOut.setOnClickListener {
            showDialog(getString(R.string.text_message_logout)){
                preferenceManager.clear()
                startActivity(Intent(applicationContext, SignInActivity::class.java))
                finish()
            }
        }

    }

    private fun showDeleteDialog() {
        showDialog("测试测试测试"){
            displayToast("完美完美完美")
        }
//        if (dialogDelete == null) {
//            val builder = AlertDialog.Builder(this)
//            val view: View = LayoutInflater.from(this).inflate(
//                R.layout.layout_alert_dialog,
//                findViewById(R.id.layoutAlertContainer)
//            )
//            builder.setView(view)
//            dialogDelete = builder.create()
//            if (dialogDelete.window != null) {
//                dialogDelete.window!!.setBackgroundDrawable(ColorDrawable(0))
//            }
//            view.findViewById<TextView>(R.id.textMessage).text =
//                getString(R.string.text_message_logout)
//            view.findViewById<View>(R.id.textPositive).setOnClickListener {
                preferenceManager.clear()
                startActivity(Intent(applicationContext, SignInActivity::class.java))
                finish()
//            }
//            view.findViewById<View>(R.id.textCancel)
//                .setOnClickListener { dialogDelete.dismiss() }
//        }
//        dialogDelete.show()
    }
}