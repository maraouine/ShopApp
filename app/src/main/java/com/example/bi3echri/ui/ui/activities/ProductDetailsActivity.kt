package com.example.bi3echri.ui.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.Product
import com.example.bi3echri.utils.Constants
import com.example.bi3echri.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {
    private  var mProductID:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductID=intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product ID",mProductID)
        }
        getProductDetails()
    }
    private fun getProductDetails()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getProductDetails(this,mProductID)
    }
    fun productDetailsSuccess(product:Product)
    {
        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_title.text=product.title
        tv_product_details_price.text="$${product.price}"
        tv_product_details_description.text=product.description
        tv_product_details_available_quantity.text=product.stock_quantity


    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_details_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }

    }
}