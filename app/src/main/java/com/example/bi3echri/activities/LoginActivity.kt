package com.example.bi3echri.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.example.bi3echri.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_login.et_email as et_email1
import kotlinx.android.synthetic.main.activity_register.et_password as et_password1

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // TODO Step 1: Hide the status bar for the LoginActivity to make it full screen activity.
        // START
        // This is used to hide the status bar and make the login screen as a full screen activity.
        // It is deprecated in the API level 30. I will update you with the alternate solution soon.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        tv_register.setOnClickListener(this)
        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.tv_forgot_password -> {

                }
                R.id.btn_login -> {
                    logInRegisteredUser()
                }
                R.id.tv_register ->
                {
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }

            }

        }
    }
    private fun validateLoginDetails() :Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim(){it<=' '}) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim(){it<=' '}) -> {
                showErrorBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private  fun logInRegisteredUser()
    {
        if(validateLoginDetails())
        {
            showProgressDialog(resources.getString(R.string.please_wait))

            //Get the text from editText
            val email =et_email.text.toString().trim {it <=' '}
            val password =et_password.text.toString().trim {it <=' '}

            //Login using Firbase
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {task ->
                    //hide the progress Bar
                    hideProgressDialog()
                    if(task.isSuccessful)
                    {
                        //send user to main activity
                        showErrorBar("You are logged in sucessfully.", false)
                    }else
                    {
                        showErrorBar(task.exception!!.message.toString(),true)
                    }
                }

        }
    }
}