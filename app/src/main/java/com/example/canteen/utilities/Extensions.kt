package com.example.canteen.utilities


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.canteen.R
import com.example.canteen.application.App
import java.io.ByteArrayOutputStream

fun Context.displayToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

@SuppressLint("Range")
fun Uri.toPath(context: Context): String? {
    var path = "";
    val cursor = context.contentResolver.query(this, null, null, null, null) ?: return null;
    if (cursor.moveToFirst()) {
        try {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
    cursor.close();
    return path;
    //Uri uri = Uri.parse(path);
}

fun String.showToast() {
    Toast.makeText(App.context, this, Toast.LENGTH_LONG).show()
}

fun String.showLog() {
    Log.d("TAG", this)
}

fun Bitmap.encodeString(): String {
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

fun ImageView.loadImage(url: String?) {
    val imageUrl = Constants.NETWORK_DOMAIN + url
    Glide.with(context).load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun Context.getPreferenceManager(): PreferenceManager {
    return PreferenceManager(this)
}

/**
 * 单选对话框
 */
fun Context.singleChoiceDialog(
    title: String = "货物的类型：",
    items: Array<String>,
    OnCheckedListener: (Int) -> Unit
) {
    val builder = AlertDialog.Builder(this)
    var checkedItem = 0
    builder.setTitle(title)
    builder.setSingleChoiceItems(items, checkedItem) { _, which ->
        checkedItem = which
    }
    builder.setPositiveButton(
        "确认"
    ) { dialog, _ ->
        OnCheckedListener(checkedItem)
        dialog.dismiss() }
    builder.setNegativeButton(
        "取消"
    ) { dialog, _ -> dialog.dismiss() }
    val dialog = builder.create() //创建AlertDialog对象
    dialog.show() //显示对话框
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