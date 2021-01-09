package com.example.bi3echri.ui.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bi3echri.R
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_settings.*

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setupActionBar()
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_add_product_activity)
        val actionBar = supportActionBar
        if(actionBar !=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_add_product_activity.setNavigationOnClickListener{onBackPressed()}

    }
}