package com.gini_apps.memorygame.model.entity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val name:String, var score:String):Parcelable {
}