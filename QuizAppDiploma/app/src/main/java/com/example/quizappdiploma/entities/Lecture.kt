package com.example.quizappdiploma.entities

import android.os.Parcel
import android.os.Parcelable

data class Lecture(
    val id : Int,
    val image: Int,
    val name:String,
    val description:String
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeInt(id)
        p0.writeInt(image)
        p0.writeString(name)
        p0.writeString(description)
    }

    companion object CREATOR : Parcelable.Creator<Lecture> {
        override fun createFromParcel(parcel: Parcel): Lecture {
            return Lecture(parcel)
        }

        override fun newArray(size: Int): Array<Lecture?> {
            return arrayOfNulls(size)
        }
    }
}
