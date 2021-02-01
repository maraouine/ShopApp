package com.example.bi3echri.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address  (
val user_id:String="",
val name : String="",
val mobileNumber :String ="",

val address:String="",
val zipCode:String="",
val additionnalNote:String="",

val type:String="",
val otherDetails:String="",
var id:String=""
        ): Parcelable