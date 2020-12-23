package com.example.bi3echri.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bi3echri.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*
import android.widget.Toast.makeText as makeText1
import kotlinx.android.synthetic.main.activity_forgot_password.et_email as et_email1

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupActionBar()
    }
    private fun setupActionBar ()
    {
        setSupportActionBar(toolbar_forgot_password_activity)
        val actionBar=supportActionBar
        if(actionBar !=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_colar_back_24dp)
        }
        toolbar_forgot_password_activity.setNavigationOnClickListener {onBackPressed()}

        btn_submit.setOnClickListener{
            val email:String=et_email.text.toString().trim {it <= ' '}
            if(email.isEmpty())
            {
                showErrorBar(resources.getString(R.string.err_msg_enter_email), errorMessage = true)
            }
            else
            {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        task ->
                        hideProgressDialog()
                        if(task.isSuccessful) {
                            //show the toast message and finsih the forgot password
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                    resources.getString(R.string.email_send_sucess),
                                Toast.LENGTH_LONG

                            ).show()
                            finish()
                        }
                        else
                        {
                            showErrorBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }

    }
}