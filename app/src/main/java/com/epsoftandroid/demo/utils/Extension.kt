package com.epsoftandroid.demo.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.epsoftandroid.demo.R

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

fun showAlertDialog(
    context: Context,
    title: String,
    message: String,
    listener: DialogInterface.OnClickListener
): AlertDialog? {
    var titleValue = title
    if (context is Activity && !context.isFinishing) {
        val alertDialog: AlertDialog =
            AlertDialog.Builder(context, R.style.MyDialogTheme).create()

        titleValue =
            if (TextUtils.isEmpty(titleValue)) context.getResources().getString(R.string.app_name) else titleValue

        alertDialog.setTitle(titleValue)

        // Setting Dialog Message
        alertDialog.setMessage(Html.fromHtml("<font color='#0f1e3d'>$message</font>"))


        // Setting Cancel Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", listener)

        // Setting OKAY Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", listener)

        // Showing Alert Message
        alertDialog.show()

        return alertDialog
    }
    return null
}