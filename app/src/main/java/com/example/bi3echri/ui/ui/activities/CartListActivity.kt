package com.example.bi3echri.ui.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.CartItem
import com.example.bi3echri.models.Product
import com.example.bi3echri.ui.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_product_details.*

class CartListActivity : BaseActivity() {

    private lateinit var mProductsList:ArrayList<Product>
    private lateinit var mCartListItems:ArrayList<CartItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()
    }

    fun successCartItemsList(cartList : ArrayList<CartItem>)
    {
        hideProgressDialog()
        for (product in mProductsList)
        {
            for(cartItem in cartList)
            {
                if(product.product_id==cartItem.product_id)
                {
                    cartItem.stock_quantity = product.stock_quantity
                    if(product.stock_quantity.toInt()==0)
                    {
                        cartItem.cart_quantity=product.stock_quantity
                    }
                }
            }
        }
        mCartListItems=cartList
        if(cartList.size>0)
        {
            rv_cart_items_list.visibility=View.VISIBLE
            ll_checkout.visibility=View.VISIBLE
            tv_no_cart_item_found.visibility=View.GONE
            rv_cart_items_list.layoutManager=LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@CartListActivity,cartList)
            rv_cart_items_list.adapter=cartListAdapter

            var subTotal:Double=0.0
            for(item in mCartListItems)
            {
                val availableQuantity=item.stock_quantity.toInt()
                if(availableQuantity>0)
                {
                    val price=item.price.toDouble()
                    val quantity=item.cart_quantity.toInt()
                    subTotal+=(price*quantity)
                }

            }
            tv_sub_total.text="$$subTotal"
            tv_shipping_charge.text="$10.0"

            if(subTotal>0)
            {
             ll_checkout.visibility=View.VISIBLE
             val total=subTotal+10 // TO DO IMPLEMENT LOGIC FOR FEE
             tv_total_amount.text="$$total"
            }
            else
            {
                ll_checkout.visibility=View.GONE
            }
        }
        else
        {
            rv_cart_items_list.visibility=View.GONE
            ll_checkout.visibility=View.GONE
            tv_no_cart_item_found.visibility=View.VISIBLE
        }
    }

    fun successProductsListFromFireStore(productsList:ArrayList<Product>)
    {
        hideProgressDialog()
        mProductsList=productsList
        getCartItemsList()
    }
    private fun getProductList()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getAllProductList(this)
    }
    private fun getCartItemsList()
    {
        FirstoreClass().getCartList(this@CartListActivity)
    }

    fun itemUpdateSuccess()
    {
        hideProgressDialog()
        getCartItemsList()
    }
    override fun onResume() {
        super.onResume()
       // getCartItemsList()
        getProductList()
    }
    fun itemRemovedSucess()
    {
        hideProgressDialog()
        Toast.makeText(
            this@CartListActivity,
             resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()
        getCartItemsList()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_cart_list_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }
}