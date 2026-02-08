package com.kvrae.easykitchen.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

fun openYoutube(
    uri: String,
    context: Context,
    errorText: String = "Youtube not installed"
) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.google.android.youtube")
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            errorText,
            Toast.LENGTH_SHORT
        ).show()
    }
}


fun openUrl(
    uri: String,
    context: Context,
    errorText: String = "Browser not installed"
) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            errorText,
            Toast.LENGTH_SHORT
        ).show()
    }
}

