package com.example.bi3echri.ui.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bi3echri.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private  var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog: Dialog

    fun showErrorBar(message:String, errorMessage: Boolean)
    {
        val sanckBar=
            Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView=sanckBar.view

        if(errorMessage)
        {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(

                    this@BaseActivity,R.color.colarSnackBarError
                )
            )
        }
        else
        {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,R.color.colarSnackBarSuccess
                )
            )
        }
        sanckBar.show()
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.setText(text)

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    fun hideProgressDialog ()
    {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit ()
    {
      if(doubleBackToExitPressedOnce)
      {
          super.onBackPressed()
          return
      }
        this.doubleBackToExitPressedOnce=true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()
        @Suppress ("DEPRECATION")
        android.os.Handler().postDelayed({doubleBackToExitPressedOnce=false},2000)
    }
}