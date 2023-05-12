package com.example.snacksprint.home_activity.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.snacksprint.R
import com.example.snacksprint.cart.model.CartModel
import com.example.snacksprint.home_activity.model.Drink
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs


class DrinkAdapter : RecyclerView.Adapter<DrinkAdapter.MyViewHolder> {
    var drinkList: List<Drink>? = null
    private var listener: OnItemClickListener? = null
    var context: Context? = null
    private var cartModelList: MutableList<CartModel> = mutableListOf()
    private val gson = Gson()

    constructor(drinkList: List<Drink>?, context: Context) : super() {
        this.drinkList = drinkList
        this.context = context
        cartModelList.addAll(getCartItems())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.popular_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menu = drinkList!![position]
        val imageUrl = menu.strDrinkThumb
        holder.tvTitle.text = menu.strDrink
        holder.tvPrice.text = "Kes ${menu.idDrink} /="
        Glide.with(holder.itemView)
            .load(imageUrl)
            .placeholder(R.drawable.famous)
            .into(holder.ivCocktail)
        holder.view.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onItemSelected(menu, position)
            }
        }

        holder.cvAddCart.setOnClickListener {
            val cartItem = CartModel(
                name = menu.strDrink,
                price = menu.idDrink.toDouble(),
                units = "1",
                imageUrl = menu.strDrinkThumb
            )
            addCartItem(cartItem)
            Toast.makeText(context,"Added to cart",Toast.LENGTH_SHORT).show()
        }
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return drinkList!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvName)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var cvAddCart: CardView = itemView.findViewById(R.id.cvAddCart)
        var ivCocktail: ImageView = itemView.findViewById(R.id.ivCocktail)
        var view: View = itemView
    }

    interface OnItemClickListener {
        fun onItemSelected(Drink: Drink?, position: Int)
    }

    fun addCartItem(cartModel: CartModel) {
        if (!cartModelList.contains(cartModel)) {
            // Add the new cart item
            cartModelList.add(cartModel)
        } else {
            Log.d("onCartError", "${cartModel.name} has already been added to the list!")
        }

        // Convert the updated list to JSON string
        val updatedCartItemsJson = gson.toJson(cartModelList)

        // Store the updated list in EasyPrefs
        Prefs.putString("cartItemsList", updatedCartItemsJson)
    }

    private fun getCartItems(): MutableList<CartModel> {
        return cartModelList
    }
}
