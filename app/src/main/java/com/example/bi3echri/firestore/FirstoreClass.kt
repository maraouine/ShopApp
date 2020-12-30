package com.example.bi3echri.firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.bi3echri.activities.BaseActivity
import com.example.bi3echri.activities.LoginActivity
import com.example.bi3echri.activities.RegisterActivity
import com.example.bi3echri.activities.UserProfilActivity
import com.example.bi3echri.models.User
import com.example.bi3echri.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirstoreClass
{
    private val mFirestore=FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo : User)
    {
        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnCanceledListener {
                activity.userRegistrationSuccess()

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )
            }
    }
    fun getCurrentUserID(): String
    {
        val currentUser =FirebaseAuth.getInstance().currentUser
        var currentUserID=""
        if(currentUser!=null)
        {
            currentUserID =currentUser.uid
        }
        return  currentUserID
    }

    fun getUsersDetails(activity: Activity)
    {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())

                val user=document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(Constants.BI3ECHRI_PREFERENCES,
                    Context.MODE_PRIVATE)

                 val editor:SharedPreferences.Editor =sharedPreferences.edit()
                //key : logged in username
                //value
                    editor.putString(Constants.LOGGED_IN_USERNAME,
                    "${user.firstname} ${user.lastname}")
                editor.apply()
                //pass the result to the login activity
                when(activity)
                {
                    is LoginActivity -> {
                        activity.userLoggedInSucess(user)
                    }
                }
            }
            .addOnFailureListener{e->
            //if there is an error hide the progress dialog
                when(activity)
                {
                    is LoginActivity ->   {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName, "Error while getting user details",
                    e
                )
            }
    }
    fun updateUserProfilData(activity: Activity, userHashMap:HashMap <String, Any> )
    {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener{
                when (activity) {
                    is UserProfilActivity ->
                    {
                        activity.userProfilUpdateSucess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfilActivity ->
                    {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details",
                    e
                )
            }

    }
}
