package com.example.bi3echri.ui.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.Address
import com.example.bi3echri.ui.ui.adapters.AddressListAdapter
import com.example.bi3echri.utils.Constants
import com.example.bi3echri.utils.SwipeToDeleteCallback
import com.example.bi3echri.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_address_list.tv_title as tv_title1

class AddressListActivity : BaseActivity() {

    private var mSlectedAddress:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        if(intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS))
        {
            mSlectedAddress=intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS,false)
        }
        setupActionBar()

        if(mSlectedAddress==true)
        {
            tv_title.text=resources.getString(R.string.title_select_address)
        }
        tv_add_address.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        }

        getAddressList()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK)
        {
            getAddressList()
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "To add the address.")
        }
    }

    fun successAddressListFromFirestore(addressList:ArrayList<Address>)
    {
        hideProgressDialog()
        if(addressList.size>0)
        {
            rv_address_list.visibility= View.VISIBLE
            tv_no_address_found.visibility= View.GONE
            rv_address_list.layoutManager=LinearLayoutManager(this@AddressListActivity)
            rv_address_list.setHasFixedSize(true)
            val addressAdapter=AddressListAdapter(this,addressList,mSlectedAddress)
            rv_address_list.adapter=addressAdapter

            if(!mSlectedAddress)
            {
                val editSwipeHandler= object : SwipeToEditCallback (this)
                {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adpter=rv_address_list.adapter as AddressListAdapter
                        adpter.notifyEditItem(
                            this@AddressListActivity,
                            viewHolder.adapterPosition
                        )
                    }

                }
                val editItemTouchHelper= ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(rv_address_list)
                val deleteSwipeHandler= object : SwipeToDeleteCallback(this)
                {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        FirstoreClass().DeletedAddress(this@AddressListActivity,addressList[viewHolder.adapterPosition].id)
                    }

                }
                val DeleteItemTouchHelper= ItemTouchHelper(deleteSwipeHandler)
                DeleteItemTouchHelper.attachToRecyclerView(rv_address_list)
            }
        }
        else
        {
            rv_address_list.visibility= View.GONE
            tv_no_address_found.visibility= View.VISIBLE
        }
    }

    private fun getAddressList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getAddressList(this)
    }

    fun deletedAddressSuccess()
    {
        hideProgressDialog()
        Toast.makeText(
            this@AddressListActivity,
            resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()
        getAddressList()

    }
    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_address_list_activity)
        val actionBar = supportActionBar
        if(actionBar !=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_address_list_activity.setNavigationOnClickListener{onBackPressed()}

    }
}