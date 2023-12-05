package com.epsoftandroid.demo.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.epsoftandroid.demo.R

object BindingUtil {

    @BindingAdapter("imageUrl", "errorDrawble")
    @JvmStatic
    fun setImage(imageView: ImageView, url: String?, errorDrawable: Drawable?) {
        var errorDrawable = errorDrawable
        if (url == null || url.isEmpty()) {
            return
        }
        val context = imageView.context
        val res = context.resources
        if (errorDrawable == null) {
            errorDrawable =
                ContextCompat.getDrawable(
                    imageView.context,
                    R.drawable.ic_image_placeholder_wrapper
                )
        }
        Glide.with(context).load(url)
            .placeholder(errorDrawable).error(errorDrawable).into(imageView)
    }
}