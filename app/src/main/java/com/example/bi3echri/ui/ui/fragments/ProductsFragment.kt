package com.example.bi3echri.ui.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.Product
import com.example.bi3echri.ui.ui.activities.AddProductActivity
import com.example.bi3echri.ui.ui.activities.SettingsActivity
import com.example.bi3echri.ui.ui.adapters.MyProductsListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

open class ProductsFragment : BaseFragment() {

    //private lateinit var homeViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //homeViewModel =ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_products, container, false)
        return root
    }
    override  fun onResume()
    {
        super.onResume()
        getProductsListFromFireStore()
    }
    fun successProductsListFromFirestore(productsList:ArrayList<Product>)
    {
        hideProgressDialog()
        if(productsList.size>0)
        {
            rv_my_product_items.visibility=View.VISIBLE
            tv_no_products_found.visibility=View.GONE

            rv_my_product_items.layoutManager=LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProducts=MyProductsListAdapter(requireActivity(),productsList)
            rv_my_product_items.adapter=adapterProducts

        }
        else
        {
            rv_my_product_items.visibility=View.GONE
            tv_no_products_found.visibility=View.VISIBLE
        }

    }
    private fun getProductsListFromFireStore()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getProductList(this)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id)
        {
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}