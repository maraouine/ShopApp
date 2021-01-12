package com.example.bi3echri.ui.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.Product
import com.example.bi3echri.utils.Constants
import com.example.bi3echri.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.iv_user_photo
import kotlinx.android.synthetic.main.activity_user_profil.*
import java.io.IOException
import java.net.URI
import java.util.jar.Manifest

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mselectedImageFileURI :Uri? =null
    private var mProductImagURL : String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setupActionBar()
        iv_add_update_product.setOnClickListener(this)
        btn_submit_add_product.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_add_product_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_add_product_activity.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@AddProductActivity)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_submit_add_product -> {
                    if(validateUserProductDetails()){
                        uploadProductImage()
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

        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==Constants.PICK_IMAGE_REQUEST_CODE)
            {
                if(data!=null)
                {
                    iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_vector_edit
                    ))
                     mselectedImageFileURI = data.data!!
                    try {
                        GlideLoader(this).loadProductPicture(mselectedImageFileURI!!,iv_product_image)
                    }
                    catch (e : IOException)
                    {
                        e.printStackTrace()
                    }
                }
            }
        }
        else if(resultCode==Activity.RESULT_CANCELED)
        {
            Log.e("Request Cancelled","Image selection cancelled")
        }
    }
    private fun validateUserProductDetails():Boolean{
        return when {
            mselectedImageFileURI==null ->
            {
                showErrorBar(resources.getString(R.string.err_msg_select_product_image),true)
                false
            }
            TextUtils.isEmpty(et_product_title.text.toString().trim {it <= ' ' }) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_product_title),true)
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim {it <= ' ' }) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_product_price),true)
                false
            }
            TextUtils.isEmpty(et_product_description.text.toString().trim {it <= ' ' }) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_product_description),true)
                false
            }
            TextUtils.isEmpty(et_product_quantity.text.toString().trim {it <= ' ' }) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_product_quantity),true)
                false
            }
            else ->
            {
                true
            }
        }
    }

    private  fun uploadProductImage()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().uploadImageToCouldStorage(this,mselectedImageFileURI,Constants.PRODUCT_IMAGE)

    }

    fun productUploadSuccess()
    {
        hideProgressDialog()
        Toast.makeText(
            this@AddProductActivity,
            resources.getString(R.string.product_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()
        finish()

    }
    fun imageUploadSuccess (imageURL : String)
    {
        mProductImagURL=imageURL
        uploadProductDetails()
    }

    private  fun uploadProductDetails()
    {
        val username=this.getSharedPreferences(Constants.BI3ECHRI_PREFERENCES,Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME,"")!!

        val product = Product(
            FirstoreClass().getCurrentUserID(),
            username,
            et_product_title.text.toString().trim { it <= ' '},
            et_product_price.text.toString().trim { it <= ' '},
            et_product_description.text.toString().trim { it <= ' '},
            et_product_quantity.text.toString().trim { it <= ' '},
            mProductImagURL
            )
        FirstoreClass().uploadProductDetails(this, product)
    }
}