package com.example.bi3echri.ui.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.Address
import com.example.bi3echri.models.CartItem
import com.example.bi3echri.models.Order
import com.example.bi3echri.models.Product
import com.example.bi3echri.ui.ui.adapters.CartItemsListAdapter
import com.example.bi3echri.utils.Constants
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address?=null
    private lateinit var mProductsList:ArrayList<Product>
    private lateinit var mCartItemsList:ArrayList<CartItem>
    private var mSubTotal:Double=0.0
    private var mTotalAmount:Double=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS))
        {
            mAddressDetails=intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)
        }
        if(mAddressDetails!=null)
        {
            tv_checkout_address_type.text=mAddressDetails?.type
            tv_checkout_full_name.text=mAddressDetails?.name
            tv_checkout_address.text="${mAddressDetails!!.address},${mAddressDetails!!.zipCode}"
            tv_checkout_additional_note.text=mAddressDetails?.additionnalNote
        if(mAddressDetails?.otherDetails!!.isNotEmpty())
        {
            tv_checkout_other_details.text=mAddressDetails?.otherDetails
        }
            tv_checkout_mobile_number.text=mAddressDetails?.mobileNumber

        }
        getProductList()
        btn_place_order.setOnClickListener{
            placeAnOrder()
        }
    }

    fun OrderPlacedSuccess()
    {
        hideProgressDialog()
        Toast.makeText(this@CheckoutActivity, "The order was placed succefully",
        Toast.LENGTH_SHORT).show()

        val intent=Intent(this@CheckoutActivity,DashboardActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    fun successProductsListFromFireStore(productsList:ArrayList<Product>)
    {
        mProductsList=productsList
        getCartItemsList()
    }

    fun getCartItemsList()
    {
        FirstoreClass().getCartList(this@CheckoutActivity)
    }

    fun successCartItemsList(cartList:ArrayList<CartItem>)
    {
        hideProgressDialog()
        for(product in mProductsList)
        {
            for (cartITem in cartList)
            {
                if(product.product_id==cartITem.product_id){
                    cartITem.stock_quantity=product.stock_quantity

                }
            }
        }
        mCartItemsList=cartList
        rv_cart_list_items.layoutManager=LinearLayoutManager(this@CheckoutActivity)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity,mCartItemsList,false)
        rv_cart_list_items.adapter=cartListAdapter

        for(item in mCartItemsList)
        {
            val availableQuantity=item.stock_quantity.toInt()
            if(availableQuantity >0)
            {
                val price=item.price.toDouble()
                val quantity=item.cart_quantity.toInt()

                mSubTotal +=(price*quantity)
            }
        }
        tv_checkout_sub_total.text= "$$mSubTotal"
        tv_checkout_shipping_charge.text="$10.0"

        if(mSubTotal>0)
        {
            ll_checkout_place_order.visibility= View.VISIBLE
            mTotalAmount= mSubTotal  + 10.0
            tv_checkout_total_amount.text="$$mTotalAmount"
        }
        else
        {
            ll_checkout_place_order.visibility= View.GONE
        }
    }

    private fun getProductList()
    {
       //show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getAllProductList(this@CheckoutActivity)
    }

    private fun placeAnOrder()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        if(mAddressDetails!=null)
        {
            val order = Order(
                FirstoreClass().getCurrentUserID(),
                mCartItemsList,
                mAddressDetails!!,
                "My order ${System.currentTimeMillis()}",
                mCartItemsList[0].image, // FIRST ENTRY ON THE CART ITEM LIST
                mSubTotal.toString(),
                "10.0", // CREALTE AN ALGO TO CALCULATE SHIPPING FEES
                mTotalAmount.toString()
                )
            FirstoreClass().placeOrder(this,order)
        }

    }
    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_checkout_activity)
        val actionBar = supportActionBar
        if(actionBar !=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_checkout_activity.setNavigationOnClickListener{onBackPressed()}

    }
}