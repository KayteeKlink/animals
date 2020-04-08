package com.example.animals.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.example.animals.R
import com.example.animals.model.Animal
import com.example.animals.util.getProgressDrawable
import com.example.animals.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    var animal: Animal? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            animal = DetailFragmentArgs.fromBundle(it).animal
        } //it refers to the argument bundle

        context?.let {
            animalImage.loadImage(animal?.imageUrl, getProgressDrawable(it)) //it refers to context
            //loads our image
        }

        animalName.text = animal?.name
        animalLocation.text = animal?.location
        animalLifespan.text = animal?.lifeSpan
        animalDiet.text = animal?.diet

        animal?.imageUrl?.let {
            setupBackgroundColor(it) //if the image url is not null then call this to use it to set the bg color
        }
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
                        animalLayout.setBackgroundColor(intColor)
                    }
            }

        })
}

}
