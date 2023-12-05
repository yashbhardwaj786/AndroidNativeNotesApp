package com.epsoftandroid.demo.utils

import android.content.Context
import android.content.res.ColorStateList
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat

fun showToast(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun setButtonActivatedWithColor(
    context: Context?,
    button: Button?,
    color: Int
) {
    if (button != null) {
        button.isEnabled = true
        val c: ColorStateList = ContextCompat.getColorStateList(context!!, color)!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.backgroundTintList = c
        }
    }
}

fun setButtonDeactivated(
    context: Context?,
    button: Button?,
    isButtonEnabled: Boolean?,
    color: Int
) {
    if (button != null) {
        button.isEnabled = isButtonEnabled!!
        val c: ColorStateList =
            ContextCompat.getColorStateList(context!!, color)!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.backgroundTintList = c
        }
    }
}