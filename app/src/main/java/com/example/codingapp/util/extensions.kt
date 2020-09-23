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


fun ImageView.loadImage(view: ImageView, url: String?) {
//    view.load(url){
//        placeholder(getProgressDrawable(view.context))
//        error(R.mipmap.ic_launcher_round)
//    }

    val options = RequestOptions()
        .placeholder(getProgressDrawable(view.context))
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)


}