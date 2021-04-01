package com.example.bi3echri.ui.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.Address
import com.example.bi3echri.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_add_edit_address.tv_title as tv_title1

class AddEditAddressActivity : BaseActivity() {

    private  var mAddressDetails : Address?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        setupActionBar()
        if(intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS))
        {
            mAddressDetails=intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }
        if(mAddressDetails!=null)
        {
            if(mAddressDetails!!.id.isNotEmpty())
            {
                tv_title.text=resources.getString(R.string.title_edit_address)
                btn_submit_address.text=resources.getString(R.string.edit_btn_update)

                et_full_name.setText(mAddressDetails?.name)
                et_phone_number.setText(mAddressDetails?.mobileNumber)
                et_address.setText(mAddressDetails?.address)
                et_zip_code.setText(mAddressDetails?.zipCode)
                et_additional_note.setText(mAddressDetails?.additionnalNote)

                when (mAddressDetails?.type)
                {
                    Constants.HOME -> {
                        rb_home.isChecked=true
                    }
                    Constants.OFFICE -> {
                        rb_home.isChecked=true
                    }
                   else -> {
                       rb_other.isChecked=true
                       til_other_details.visibility=View.VISIBLE
                       et_other_details.setText(mAddressDetails?.otherDetails)
                    }
                }
            }

        }
        btn_submit_address.setOnClickListener{
            saveAddressToFirestore()
        }
        rg_type.setOnCheckedChangeListener{_,checkedID->
            if(checkedID==R.id.rb_other)
            {
                til_other_details.visibility=View.VISIBLE
            }
            else
            {
                til_other_details.visibility=View.GONE
            }
        }
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_add_edit_address_activity)
        val actionBar = supportActionBar
        if(actionBar !=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_add_edit_address_activity.setNavigationOnClickListener{onBackPressed()}

    }
    private  fun saveAddressToFirestore() {
        val fullName: String = et_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = et_phone_number.text.toString().trim { it <= ' ' }
        val address: String = et_address.text.toString().trim { it <= ' ' }
        val zipCode: String = et_zip_code.text.toString().trim { it <= ' ' }
        val additionnalNote: String = et_full_name.text.toString().trim { it <= ' ' }
        val otherDetails: String = et_full_name.text.toString().trim { it <= ' ' }

        if (validateData()) {
            showProgressDialog(resources.getString(R.string.please_wait))
                val addressType: String = when {
                    rb_home.isChecked -> {
                        Constants.HOME
                    }
                    rb_office.isChecked -> {
                        Constants.OFFICE
                    }
                    else -> {
                        Constants.OTHER
                    }
                }
                val addressModel=Address(
                    FirstoreClass().getCurrentUserID(),
                    fullName,
                    phoneNumber,
                    address,
                    zipCode,
                    additionnalNote,
                    addressType,
                    otherDetails
                )
            if(mAddressDetails !=null && mAddressDetails!!.id.isNotEmpty())
            {
                FirstoreClass().updatedAddress(this,addressModel,mAddressDetails!!.id)
            }
            else{
                FirstoreClass().addAddress(this,addressModel)
            }

            }
        }
    fun addUpdateAcdressSuccess()
    {
        hideProgressDialog()

        val notifiySuccessMessage:String=if(mAddressDetails!=null && mAddressDetails!!.id.isNotEmpty())
        {
            resources.getString(R.string.edit_your_address_added_successfully)
        }else
        {
            resources.getString(R.string.err_your_address_added_successfully)
        }
        Toast.makeText(
            this@AddEditAddressActivity,
           notifiySuccessMessage,
            Toast.LENGTH_SHORT
        ).show()
        setResult(RESULT_OK)
        finish()
    }
    private fun validateData():Boolean
    {
        return when {
            TextUtils.isEmpty(et_full_name.text.toString().trim {it <= ' '})->{
                showErrorBar(resources.getString(R.string.err_msg_please_enter_full_name),
                    true
                )
                false
            }
            TextUtils.isEmpty(et_phone_number.text.toString().trim {it <= ' '})->{
                showErrorBar(resources.getString(R.string.err_msg_please_enter_full_number),
                    true
                )
                false
            }
            TextUtils.isEmpty(et_address.text.toString().trim {it <= ' '})->{
                showErrorBar(resources.getString(R.string.err_msg_please_enter_address),
                    true
                )
                false
            }
            TextUtils.isEmpty(et_zip_code.text.toString().trim {it <= ' '})->{
                showErrorBar(resources.getString(R.string.err_msg_please_zip_code),
                    true
                )
                false
            }
            rb_other.isChecked && TextUtils.isEmpty(
                et_zip_code.text.toString().trim { it <= ' ' }) -> {
                   showErrorBar(resources.getString(R.string.err_msg_please_enter_zip_code)
                   ,true)
                false
                }
            else -> {
                true
            }

        }
    }
}