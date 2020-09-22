package com.example.codingapp.util

import android.content.Context
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

//Creating extension function which we will load in AnimalListAdapter
fun ImageView.loadImage(circularProgressDrawable: CircularProgressDrawable, uri: String?) {
    val options = RequestOptions()
        .placeholder(circularProgressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)

}

fun loadImage(view:ImageView,url: String?){
    view.loadImage(getProgressDrawable(view.context),url)
}

