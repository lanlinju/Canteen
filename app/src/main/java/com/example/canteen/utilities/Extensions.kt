package com.example.canteen.utilities

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.canteen.R

fun Context.displayToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Context.showDialog(message: String, onClick: (view: View) -> Unit) {
    val builder = AlertDialog.Builder(this)
    val view: View = LayoutInflater.from(this).inflate(
        R.layout.layout_alert_dialog,
        null
    )
    builder.setView(view)
    val alertDialog = builder.create()
    if (alertDialog.window != null) {
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
    }
    view.findViewById<TextView>(R.id.textMessage).text = message
    view.findViewById<View>(R.id.textPositive).setOnClickListener {
        onClick(it)
        alertDialog.dismiss()
    }
    view.findViewById<View>(R.id.textCancel)
        .setOnClickListener { alertDialog.dismiss() }
    alertDialog.show()
}