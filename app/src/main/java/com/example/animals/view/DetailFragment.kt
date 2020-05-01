package com.example.animals.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.example.animals.R
import com.example.animals.databinding.FragmentDetailBinding
import com.example.animals.model.Animal


class DetailFragment : Fragment() {

    var animal: Animal? = null
    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            animal = DetailFragmentArgs.fromBundle(it).animal
        } //it refers to the argument bundle

        animal?.imageUrl?.let {
            setupBackgroundColor(it) //if the image url is not null then call this to use it to set the bg color
        }

        dataBinding.animal = animal
    }

fun setupBackgroundColor(url: String) {
    //can use glide to load the url into our palette layout
    Glide.with(this)
        .asBitmap() //convert the image we get from our url variable into a bitmap
        .load(url)
        .into(object : CustomTarget<Bitmap>(){
            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                Palette.from(resource) //resource has been retrieved from the url
                    //will call the pallete library and generate the colors that are available
                    .generate() { palette ->
                        //some of these colors might be null for some of the images, so irl would prob wanna check if it's null and if so use a new colors
                        val intColor = palette?.lightMutedSwatch?.rgb ?: 0 //if it is null, setting it to 0 (so that it's never be null while we're using it)
                        dataBinding.animalLayout.setBackgroundColor(intColor)
                    }
            }

        })
}

}
