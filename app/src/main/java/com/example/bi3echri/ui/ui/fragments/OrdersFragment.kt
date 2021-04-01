package com.example.bi3echri.ui.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.Order
import com.example.bi3echri.ui.ui.adapters.MyOrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_products.*

class OrdersFragment : BaseFragment() {

    override  fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_orders, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        getFragmentMyOrderList()
    }
    private fun getFragmentMyOrderList()
    {

        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getMyOrdersList(this@OrdersFragment)
    }
    fun populateOrdersListInUI(orderList:ArrayList<Order>)
    {
        hideProgressDialog()

        Log.e("OREDER LIST", orderList.size.toString())


        if(orderList.size > 0)
        {
            rv_my_order_items.visibility=View.VISIBLE
            tv_no_orders_found.visibility=View.GONE
            rv_my_order_items.layoutManager=LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val myOrdersAdapter=MyOrdersListAdapter(requireActivity(),orderList)
            rv_my_order_items.adapter=myOrdersAdapter
        }
        else
        {
            rv_my_order_items.visibility=View.GONE
            tv_no_orders_found.visibility=View.VISIBLE
        }



    }



}