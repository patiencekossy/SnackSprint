package com.example.snacksprint.cart.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide import com.bumptech.glide.request.RequestOptions
import com.example.snacksprint.R
import com.example.snacksprint.cart.model.CartModel


class CartAdapter(var context: Context):
    RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    var cartModel : List<CartModel> = listOf() // empty  list

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_row, parent, false)
        return ViewHolder(view)
    }
    //so far item view is same as single item
    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        /*val  tvname= holder.itemView.findViewById(R.id.tvname) as TextView
        val tvprice = holder.itemView.findViewById(R.id.tvPrice) as TextView
        val image_url = holder.itemView.findViewById(R.id.image_url) as ImageView*/


/*
        //bind
        val item = cartModel[position]
        tvname.text=item.name
        tvprice.text = item.price.toString()

        //
        Glide.with(context).load(item.imageUrl)
            .apply(RequestOptions().centerCrop())
            .into(image_url)

        //image.setImageResource(item.image)*/

    }
    override fun getItemCount(): Int { //count the number items coming from the API
        return cartModel.size
    }
    //we will call this function on Loopj response
    fun setProductListItems(conferenceRoom: List<CartModel>){
        this.cartModel = conferenceRoom
        notifyDataSetChanged()
        }
    }