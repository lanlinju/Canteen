package com.example.canteen.utilities


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.canteen.R
import com.example.canteen.application.App

fun Context.displayToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun String.showToast(){
    Toast.makeText(App.context,this, Toast.LENGTH_LONG).show()
}

fun String.showLog() {
    Log.d("TAG", this)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.notShow() {
    this.visibility = View.GONE
}

fun Context.getPreferenceManager():PreferenceManager{
    return PreferenceManager(this)
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
        onClick(view)
        alertDialog.dismiss()
    }
    view.findViewById<View>(R.id.textCancel)
        .setOnClickListener { alertDialog.dismiss() }
    alertDialog.show()
}

fun String.copy2Clipboard(context: Context) {
    try {
        val systemService: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        systemService.setPrimaryClip(ClipData.newPlainText("text", this))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun EditText.showKeyboard() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    val inputManager =
        App.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, 0)
}

fun EditText.hideKeyboard() {
    val inputManager =
        App.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, 0)
}