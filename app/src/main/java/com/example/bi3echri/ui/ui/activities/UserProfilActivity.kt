package com.example.bi3echri.ui.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.User
import com.example.bi3echri.utils.Constants
import com.example.bi3echri.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_register.et_email
import kotlinx.android.synthetic.main.activity_register.et_first_name
import kotlinx.android.synthetic.main.activity_register.et_last_name
import kotlinx.android.synthetic.main.activity_user_profil.*
import java.io.IOException

class UserProfilActivity : BaseActivity(), View.OnClickListener {
    //Global user details
    private lateinit var userDetails: User
    private var mSelectedImageFileUri: Uri?=null
    private var mUserProfilImageURL: String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profil)

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
        btn_submit.setOnClickListener(this@UserProfilActivity)
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
            R.id.btn_submit -> {
              if (validateUserProfilDetails())
                {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    if(mSelectedImageFileUri!=null){
                        FirstoreClass().uploadImageToCouldStorage(this,mSelectedImageFileUri)
                    }
                    else
                    {
                        updateUserProfilDetails()
                    }
                }
            }
        }

    }

    }
    private fun updateUserProfilDetails(){
        val userHashMap= HashMap<String, Any>()
        val mobilenumber=et_mobile_number.text.toString().trim { it <= ' '}
        val gender= if(rb_male.isChecked)
        {
            Constants.MALE
        }else
            Constants.FEMALE
        if(mobilenumber.isNotEmpty())
        {
            //stored it on the hasmap
            userHashMap[Constants.MOBILE]=mobilenumber.toLong()
        }
        if(mUserProfilImageURL.isNotEmpty())
        {
            userHashMap[Constants.IMAGE]=mUserProfilImageURL
        }
        userHashMap[Constants.GENDER]=gender
        userHashMap[Constants.COMPLETE_PROFIL]=1
        //show the progress dialog "please wait"
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().updateUserProfilData(this,userHashMap)


    }
    fun userProfilUpdateSucess()
    {
        hideProgressDialog()
        Toast.makeText(
            this@UserProfilActivity,
            resources.getString(R.string.msg_profil_update_success),
          Toast.LENGTH_SHORT).
        show()
        startActivity(Intent(this@UserProfilActivity,
            MainActivity::class.java))
        finish()
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
                        mSelectedImageFileUri=data.data!!
                        //iv_user_photo.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!,iv_user_photo)
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

    private fun validateUserProfilDetails():Boolean{
        return when {
            TextUtils.isEmpty(et_mobile_number.text.toString().trim {it <= ' ' }) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_mobile_number),true)
                false
        }
            else-> {
                true
            }


        }
    }

    fun imageUploadSuccess (imageURL : String)
    {
        mUserProfilImageURL= imageURL
        updateUserProfilDetails()
    }

}