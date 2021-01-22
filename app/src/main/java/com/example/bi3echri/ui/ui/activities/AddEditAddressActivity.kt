package com.example.bi3echri.ui.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bi3echri.R
import kotlinx.android.synthetic.main.activity_add_edit_address.*
import kotlinx.android.synthetic.main.activity_address_list.*

class AddEditAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        setupActionBar()
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
}