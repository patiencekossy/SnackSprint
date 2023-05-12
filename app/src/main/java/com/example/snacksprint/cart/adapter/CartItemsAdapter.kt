package com.example.snacksprint.cart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.snacksprint.R
import com.example.snacksprint.cart.model.CartModel
import com.example.snacksprint.home_activity.model.Drink

class CartItemsAdapter  : RecyclerView.Adapter<CartItemsAdapter.MyViewHolder> {
    var cartList: List<CartModel>? = null
    private var listener: OnItemClickListener? = null
    var context: Context? = null

    constructor(cartList: List<CartModel>?, context: Context) : super() {
        this.cartList = cartList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menu = cartList!![position]
        val imageUrl = menu.imageUrl
        holder.tvTitle.text = menu.name
        holder.tvPrice.text = "Kes ${menu.price} /="
        Glide.with(holder.itemView)
            .load(imageUrl)
            .placeholder(R.drawable.famous)
            .into(holder.ivCocktail)

        holder.view.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onItemSelected(menu, position)
            }
        }
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return cartList!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvName)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var cvCartItem: CardView = itemView.findViewById(R.id.cvCartItem)
        var ivCocktail: ImageView = itemView.findViewById(R.id.ivCartItem)
        var view: View = itemView
    }

    interface OnItemClickListener {
        fun onItemSelected(cartModel: CartModel, position: Int)
    }
}