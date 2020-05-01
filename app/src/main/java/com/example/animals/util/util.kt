package com.example.animals.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.animals.R

//use glide to retrieve our images from the url for the animal and store it
//use kotlin extensions to extend the imageview class and allow it to automatically load images for us

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f //just impacts the spinner before image displayed
        start()
    }
}

//Glide just takes a uri and does everything else. Handles errors, handles loading, network communication, caching.
//we just give the uri, what image to load, where to load it, and it does all of this in a background thread so that we don't block the user on the main thread^

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    //use glide here to actually load the image and load the spinner while waiting for the image to load, and have an error image load if needed
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this) //image view has a context and we're loading into it, aka 'this'
}

@BindingAdapter("android:imageUrl") //allows us to have a new parameter in our layout that we can pass that will call loadImage below function directly
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context)) //will automatically load our image in the view that I provide to this function
}