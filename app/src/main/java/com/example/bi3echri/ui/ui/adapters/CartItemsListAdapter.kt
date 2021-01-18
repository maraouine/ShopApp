package com.example.bi3echri.ui.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bi3echri.R
import com.example.bi3echri.models.CartItem
import com.example.bi3echri.utils.GlideLoader
import kotlinx.android.synthetic.main.item_cart_layout.view.*
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

open class CartItemsListAdapter (
    private val context: Context,
            private var list:ArrayList<CartItem>
):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
                parent,
                false
            )

        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is MyViewHolder)
        {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.iv_cart_item_image
            )
            holder.itemView.tv_cart_item_title.text=model.title
            holder.itemView.tv_cart_item_price.text="$${model.price}"
            holder.itemView.tv_cart_quantity.text=model.cart_qunatity

        }

    }


    override fun getItemCount(): Int {
        return list.size
    }
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}