package com.example.bi3echri.ui.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.CartItem
import com.example.bi3echri.models.Product
import com.example.bi3echri.utils.Constants
import com.example.bi3echri.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {
    private  var mProductID:String=""
    private lateinit var mProductDetails:Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductID=intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        var prodcutOwnerID : String=""

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            prodcutOwnerID=intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        if(FirstoreClass().getCurrentUserID()==prodcutOwnerID)
        {
            btn_add_to_cart.visibility= View.GONE
            btn_go_to_cart.visibility= View.GONE

        }
        else{
            btn_add_to_cart.visibility=View.VISIBLE
        }
        getProductDetails()
        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)
    }
    private fun getProductDetails()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getProductDetails(this,mProductID)
    }

    fun productExistsInCart()
    {
        hideProgressDialog()
        btn_add_to_cart.visibility=View.GONE
        btn_go_to_cart.visibility=View.VISIBLE
    }
    fun productDetailsSuccess(product:Product)
    {
        mProductDetails=product
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_title.text=product.title
        tv_product_details_price.text="$${product.price}"
        tv_product_details_description.text=product.description
        tv_product_details_available_quantity.text=product.stock_quantity

        if(FirstoreClass().getCurrentUserID()==product.user_id)
        {
            hideProgressDialog()
        }
        else
        {
            FirstoreClass().checkIfItemExistInCart(this,mProductID)
        }


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
    private fun addToCart()
    {
        val cartitem=CartItem(
            FirstoreClass().getCurrentUserID(),
            mProductID,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().addCartItems(this, cartitem)
    }

    fun addToCartSuccess()
    {
        hideProgressDialog()
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()

        btn_add_to_cart.visibility=View.GONE
        btn_go_to_cart.visibility=View.VISIBLE

    }
    override fun onClick(v : View?) {
        if(v!=null)
        {
            when(v.id)
            {
               R.id.btn_add_to_cart -> {
                   addToCart()
               }
                R.id.btn_go_to_cart ->
                {
                    startActivity(Intent(this@ProductDetailsActivity,CartListActivity::class.java))
                }
            }

        }
    }
}