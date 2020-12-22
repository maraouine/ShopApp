package com.example.bi3echri.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import com.example.bi3echri.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_email
import kotlinx.android.synthetic.main.activity_register.et_password

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
        // TODO Step 7: Assign a onclick event to the register text to launch the register activity.
        // START
        tv_login.setOnClickListener {
            onBackPressed()
        }
        btn_register.setOnClickListener {
            registrerUser()
        }
    }

    private fun setupActionBar ()
    {
        setSupportActionBar(toolbar_register_activity)
        val actionBar=supportActionBar
        if(actionBar !=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_colar_back_24dp)
        }
        toolbar_register_activity.setNavigationOnClickListener {onBackPressed()}
    }

    /*A function to validate entries */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim() {it <= ' '}) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_first_name), errorMessage = true)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim() {it <= ' '}) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_last_name), errorMessage = true)
                false
             }
            TextUtils.isEmpty(et_email.text.toString().trim() {it <= ' '}) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_email), errorMessage = true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim() {it <= ' '}) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_password), errorMessage = true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim() {it <= ' '}) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_password), errorMessage = true)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim() {it <= ' '}) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_confirm_password), errorMessage = true)
                false
            }

            // Compare passeword
            et_password.text.toString().trim() { it <= ' '} != et_confirm_password.text.toString().trim() {it <=' '} -> {
                showErrorBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), errorMessage = true)
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorBar(resources.getString(R.string.err_msg_agree_terms_and_condition), errorMessage = true)
                false
            }
            else -> {
                showErrorBar(resources.getString(R.string.registery_successfull), errorMessage = false)
                true
            }

        }


    }

    private  fun registrerUser ()
    {
       if(validateRegisterDetails())
       {

            showProgressDialog(resources.getString(R.string.please_wait))
            val email:String=et_email.text.toString().trim(){it<=' '}
            val password:String=et_password.text.toString().trim(){it<=' '}
           // create insrtance and create a register and user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener (
                {
                    task ->
                    hideProgressDialog()
                    if(task.isSuccessful)
                    {
                        //register user
                        val firebaseUser:FirebaseUser = task.result!!.user!!
                        showErrorBar(
                            "Your are registerd successfully. Your user id is ${firebaseUser.uid}",
                            false
                        )
                        FirebaseAuth.getInstance().signOut()
                        finish()
                    } else {
                        //if is not successfully
                        showErrorBar(task.exception!!.message.toString(),errorMessage = true)
                    }
                })
       }
    }

}