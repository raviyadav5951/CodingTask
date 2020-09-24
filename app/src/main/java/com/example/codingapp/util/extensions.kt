package com.example.codingapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.codingapp.R

fun getProgressDrawable(context: Context): CircularProgressDrawable {

    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 10f
        start()
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun ImageView.loadImage(view: ImageView, url: String?) {
    val options = RequestOptions()
        .placeholder(getProgressDrawable(view.context))
        .error(R.drawable.placeholder)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}