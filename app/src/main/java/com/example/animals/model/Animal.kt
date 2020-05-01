package com.example.animals.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

//how we map the objects in our code to the objects in the api. Will map based off how we name them, unless we differ our names, which we then want to let it know what we're mapping aka SerializedName

data class ApiKey(
    val message: String?,
    val key: String?
)

//data class does not need a body. Can have functionality in a data class if you want to, but not necessary. Can just have constructor
data class Animal(
    val name: String?,
    val taxonomy: Taxonomy?,
    val location: String?,
    val speed: Speed?,
    val diet: String?,

    @SerializedName("lifespan") //retrofit system will map the var I name (lifeSpan) to the way api has it, aka serialized name (lifespan)
    val lifeSpan: String?,

    @SerializedName("image")
    val imageUrl: String?
): Parcelable { //changed this to be Parcelable whenever we started building the part where we tap on an animal and view the detail screen
    //need this to be Parcelable so we can pass the Animal from the nav action to the detail fragment.
    //have to add the  part in the adapter also             val action = ListFragmentDirections.actionDetail(animalList[position])//this is the argument we definted in the navigation detailFragment
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Taxonomy::class.java.classLoader),
        parcel.readString(),
        parcel.readParcelable(Speed::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeParcelable(taxonomy, flags)
        parcel.writeString(location)
        parcel.writeParcelable(speed, flags)
        parcel.writeString(diet)
        parcel.writeString(lifeSpan)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Animal> {
        override fun createFromParcel(parcel: Parcel): Animal {
            return Animal(parcel)
        }

        override fun newArray(size: Int): Array<Animal?> {
            return arrayOfNulls(size)
        }
    }
}

data class Taxonomy(
    val kingdom: String?,
    val order: String?,
    val family: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kingdom)
        parcel.writeString(order)
        parcel.writeString(family)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Taxonomy> {
        override fun createFromParcel(parcel: Parcel): Taxonomy {
            return Taxonomy(parcel)
        }

        override fun newArray(size: Int): Array<Taxonomy?> {
            return arrayOfNulls(size)
        }
    }
}

data class Speed( //turned this into a Parcel to our nav argument can use the Animal Object
    val metric: String?,
    val imperial: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(), //here's where we read it
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) { //we first write the strings (i.e. metric, imperial), then we read it above
        parcel.writeString(metric)
        parcel.writeString(imperial)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Speed> {
        override fun createFromParcel(parcel: Parcel): Speed {
            return Speed(parcel)
        }

        override fun newArray(size: Int): Array<Speed?> {
            return arrayOfNulls(size)
        }
    }
}

data class AnimalPalette(var color: Int)

