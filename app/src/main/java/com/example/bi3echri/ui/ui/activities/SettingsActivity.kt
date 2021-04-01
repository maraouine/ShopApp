package com.example.bi3echri.ui.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.User
import com.example.bi3echri.utils.Constants
import com.example.bi3echri.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()

        tv_edit.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
        ll_address.setOnClickListener(this)
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_settings_activity)
        val actionBar = supportActionBar
        if(actionBar !=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_settings_activity.setNavigationOnClickListener{onBackPressed()}

    }
    private fun getUserDetails()
    {
       showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getUsersDetails(this)
    }
    fun userDetailsSuccess(user: User)
    {
        mUserDetails=user
        hideProgressDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image,iv_user_photo)
        tv_name.text="${user.firstName} ${user.lastName}"
        tv_gender.text=user.gender
        tv_email.text=user.email
        tv_mobile_number.text="${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if(v!=null)
        {
            when(v.id){

                R.id.tv_edit -> {
                  val intent = Intent(this@SettingsActivity,UserProfilActivity::class.java)
                   intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                   startActivity(intent)
                }
                R.id.btn_logout ->
                {
                    FirebaseAuth.getInstance() .signOut()
                    val intent = Intent(this@SettingsActivity,LoginActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.ll_address ->
                {
                    val intent = Intent(this@SettingsActivity,AddressListActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

}

