package com.example.canteen.utilities


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.util.Base64
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
import java.io.ByteArrayOutputStream

fun Context.displayToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun String.showToast() {
    Toast.makeText(App.context, this, Toast.LENGTH_LONG).show()
}

fun String.showLog() {
    Log.d("TAG", this)
}

fun Bitmap.toString(): String {
    val previewWidth = 150
    val previewHeight = this.height * previewWidth / this.width
    val previewBitmap = Bitmap.createScaledBitmap(this, previewWidth, previewHeight, false)
    val byteArrayOutputStream = ByteArrayOutputStream()
    previewBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
    val bytes = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun String.toBitmap(): Bitmap {
    val bytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.notShow() {
    this.visibility = View.GONE
}

fun Context.getPreferenceManager(): PreferenceManager {
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