package com.example.bi3echri.ui.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bi3echri.R
import com.example.bi3echri.firestore.FirstoreClass
import com.example.bi3echri.models.CartItem
import com.example.bi3echri.ui.ui.activities.CartListActivity
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

            if(model.cart_qunatity=="0")
            {
                holder.itemView.ib_remove_cart_item.visibility=View.GONE
                holder.itemView.ib_add_cart_item.visibility=View.GONE
                holder.itemView.tv_cart_quantity.text=context.resources.getString(R.string.lbl_out_of_stock)
                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colarSnackBarError
                    )
                )

            }
            else
            {
                holder.itemView.ib_remove_cart_item.visibility=View.VISIBLE
                holder.itemView.ib_add_cart_item.visibility=View.VISIBLE
                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSecondaryText
                    )
                )
            }
            holder.itemView.ib_delete_cart_item.setOnClickListener {
                when (context)
                {
                    is CartListActivity->{
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                }
                FirstoreClass().removeItemFromCart(context,model.id)
            }

        }

    }


    override fun getItemCount(): Int {
        return list.size
    }
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}