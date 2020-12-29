package com.example.bi3echri.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bi3echri.R
import com.example.bi3echri.models.User
import com.example.bi3echri.utils.Constants
import com.example.bi3echri.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_register.et_email
import kotlinx.android.synthetic.main.activity_register.et_first_name
import kotlinx.android.synthetic.main.activity_register.et_last_name
import kotlinx.android.synthetic.main.activity_user_profil.*
import java.io.IOException

class UserProfilActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profil)

        var userDetails: User= User()
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS))
        {
            //GET the user details from intent as a parceble extra
            userDetails=intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.isEnabled=false
        et_first_name.setText(userDetails.firstname)
        et_last_name.isEnabled=false
        et_last_name.setText(userDetails.lastname)
        et_email.isEnabled=false
        et_email.setText(userDetails.email)

        iv_user_photo.setOnClickListener(this@UserProfilActivity)
    }

    override fun onClick(v: View?) {
    if(v!=null)
    {
        when(v.id)
        {
            R.id.iv_user_photo -> {
                if(ContextCompat.checkSelfPermission(
                     this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                    )
                {
                    Constants.showImageChooser(this)
                    //showErrorBar("You already have the storage permission.", false)
                }else
                {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE
                    )

                }
            }
        }

    }

    }
    fun OnRequestPermissionsResult(
        requestCode:Int,
        permissions:Array<out String>,
        grandResults:IntArray
    ){
        super.onRequestPermissionsResult(requestCode,permissions,grandResults)
        if(requestCode==Constants.READ_STORAGE_PERMISSION_CODE)
        {
            //if permission is granted
            if(grandResults.isNotEmpty() && grandResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Constants.showImageChooser(this)
            }else
            {
                //display toast if not granted
                Toast.makeText(this,
                resources.getString(R.string.read_storage_permission_denied),
                Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK)
        {
            if(requestCode==Constants.PICK_IMAGE_REQUEST_CODE)
            {
                if(data!=null)
                {
                    try {
                        val selectedImageFileUri=data.data!!
                        //iv_user_photo.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(selectedImageFileUri,iv_user_photo)
                    }catch (e:IOException)
                    {
                        e.printStackTrace()
                        Toast.makeText(this@UserProfilActivity,
                            resources.getString(R.string.image_selection_faied),
                        Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}