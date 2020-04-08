package com.example.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.animals.R
import com.example.animals.model.Animal
import com.example.animals.util.getProgressDrawable
import com.example.animals.util.loadImage
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalListAdapter(private val animalList: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    fun updateAnimalList(newAnimalList: List<Animal>) {
        animalList.clear()
        animalList.addAll(newAnimalList)
        notifyDataSetChanged() //important bc it will notify the system tha tour list has changed and that we want to refresh our whole view and recreate the elements with the new list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun getItemCount() = animalList.size

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.view.animalName.text = animalList[position].name
        holder.view.animalImage.loadImage(animalList[position].imageUrl, getProgressDrawable(holder.view.context)) //using glide to loadImage (the method we created)
        holder.view.animalLayout.setOnClickListener{
            val action = ListFragmentDirections.actionDetail(animalList[position])//this is the argument we definted in the navigation detailFragment
            Navigation.findNavController(holder.view).navigate(action) //doesn't matter which view we pass cause the system will go up the hierarchy and find what it needs here, so can pass any view
        }
    }
}