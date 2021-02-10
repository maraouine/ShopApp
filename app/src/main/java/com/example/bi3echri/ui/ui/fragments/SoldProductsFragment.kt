package com.example.bi3echri.ui.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.SoldProduct
import com.example.bi3echri.ui.ui.adapters.SoldProductsListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_sold_products.*


/**
 * A simple [Fragment] subclass.
 * Use the [SoldProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoldProductsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }
    private fun getSoldProductsList()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getSoldProductList(this@SoldProductsFragment)
    }
    fun successSoldProductsList(soldProductList:ArrayList<SoldProduct>)
    {
        hideProgressDialog()
        Log.e("OREDER LIST", soldProductList.size.toString())

        if(soldProductList.size >0)
        {
            rv_sold_product_items.visibility=View.VISIBLE
            tv_no_sold_products_found.visibility=View.GONE

            rv_sold_product_items.layoutManager=LinearLayoutManager(activity)
            rv_sold_product_items.setHasFixedSize(true)


            val soldProductListAdapter= SoldProductsListAdapter(requireActivity(),soldProductList)
            rv_sold_product_items.adapter=soldProductListAdapter

        }else
        {
            rv_sold_product_items.visibility=View.GONE
            tv_no_sold_products_found.visibility=View.VISIBLE
        }
    }

}