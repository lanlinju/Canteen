package com.example.canteen.utilities

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    val imageUrl = Constants.NETWORK_DOMAIN + url
    Glide.with(context).load(imageUrl)
//        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

@BindingAdapter("app:visibleOrGone")
fun View.setVisibleOrGone(show: Boolean) {
    visibility = if (show) VISIBLE else GONE
}

//imageView.alpha = 0f
//Picasso.get().load(URL).noFade().into(imageView, object : Callback() {
//    fun onSuccess() {
//        imageView.animate().setDuration(300).alpha(1f).start()
//    }
