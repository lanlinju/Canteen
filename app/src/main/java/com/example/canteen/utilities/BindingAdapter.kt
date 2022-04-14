package com.example.canteen.utilities

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

@BindingAdapter("app:imageUrl")
fun ImageView.setImageUrl(url: String?) {
    val imageUrl = Constants.NETWORK_DOMAIN + url
    Glide.with(context).load(imageUrl)
//        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

@BindingAdapter("imageBitmap")
fun ImageView.setImageBitmap(imageString: String?) {
    setImageBitmap(imageString?.toBitmap())
}

@BindingAdapter("app:visibleOrGone")
fun View.setVisibleOrGone(show: Boolean) {
    visibility = if (show) VISIBLE else GONE
}

@BindingAdapter("imageUrlBlur")
fun ImageView.setBlurBackground(url: String?) {
    //先隐藏
//    ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
//        .setDuration(250).start()
    val glideUrl = Constants.NETWORK_DOMAIN + url
    Glide.with(this).asBitmap().load(glideUrl).dontAnimate().into(object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            val bitmapDrawable = BitmapDrawable(null, BlurUtils.blur(resource))
            bitmapDrawable.colorFilter = PorterDuffColorFilter(
                Color.LTGRAY, PorterDuff.Mode.MULTIPLY
            )
            setImageDrawable(bitmapDrawable)

//            //加载完背景图再显示
//            ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
//                .setDuration(250).start()
        }
    })
}


//imageView.alpha = 0f
//Picasso.get().load(URL).noFade().into(imageView, object : Callback() {
//    fun onSuccess() {
//        imageView.animate().setDuration(300).alpha(1f).start()
//    }
