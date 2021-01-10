package com.example.bi3echri.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    //Collexction in cloud firestore
    const val USERS: String ="users"
    const val PRODUCTS: String ="products"

    const val BI3ECHRI_PREFERENCES: String ="bi3echriPrefs"
    const val LOGGED_IN_USERNAME:String="logged_in_username"
    const val EXTRA_USER_DETAILS:String="extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE=2
    const val PICK_IMAGE_REQUEST_CODE=1

    const val MALE:String="male"
    const val FEMALE:String="female"
    const val FIRSTNAME:String="firstName"
    const val LASTNAME:String="lastName"
    const val MOBILE:String="mobile"
    const val GENDER:String="gender"
    const val IMAGE: String ="image"
    const val COMPLETE_PROFIL: String ="profileCompleted"

    const val PRODUCT_IMAGE: String ="Product_Image"


    const val USER_PROFIL_IMAGE:String="user_profil_image"


    fun showImageChooser(activity: Activity)
    {
        val galleryIntent=Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        //LAUNCH IMMAGE SELCTION
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?
    {
        return MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(activity.contentResolver.getType(uri!!))
    }

}