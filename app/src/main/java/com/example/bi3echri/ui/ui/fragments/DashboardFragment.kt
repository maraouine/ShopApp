package com.example.bi3echri.ui.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.Product
import com.example.bi3echri.ui.ui.activities.CartListActivity
import com.example.bi3echri.ui.ui.activities.ProductDetailsActivity
import com.example.bi3echri.ui.ui.activities.SettingsActivity
import com.example.bi3echri.ui.ui.adapters.DashboardItemsListAdapter
import com.example.bi3echri.ui.ui.adapters.MyProductsListAdapter
import com.example.bi3echri.utils.Constants
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_products.*

class DashboardFragment : BaseFragment() {

   // private lateinit var dashboardViewModel: DashboardViewModel

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
      //  dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }
    override fun onResume() {
        super.onResume()
        getDashBoardItemList()
    }

    fun successDashBordItemsList(dashbordItemsList:ArrayList<Product>)
    {
        hideProgressDialog()
        if(dashbordItemsList.size>0)
        {
            rv_dashboard_items.visibility=View.VISIBLE
            tv_no_dashboard_items_found.visibility=View.GONE

            rv_dashboard_items.layoutManager= GridLayoutManager(activity,2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapter= DashboardItemsListAdapter(requireActivity(),dashbordItemsList)
            rv_dashboard_items.adapter=adapter

           /* adapter.setOnClickListener(object : DashboardItemsListAdapter.OnclickListener {
                override fun onClick(position: Int, product: Product) {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID,product.product_id)
                    startActivity(intent)
                }
            }
            )*/

        }
        else
        {
            rv_dashboard_items.visibility=View.GONE
            tv_no_dashboard_items_found.visibility=View.VISIBLE
        }


    }
    private fun getDashBoardItemList()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirstoreClass().getDashBoardItemsList(this)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id)
        {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}