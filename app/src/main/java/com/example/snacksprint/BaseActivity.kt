package com.example.snacksprint

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.snacksprint.cart.model.CartModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pixplicity.easyprefs.library.Prefs


abstract class BaseActivity: AppCompatActivity() {
    var cartItemsList: MutableList<CartModel> = ArrayList()
    private var cartModelList: MutableList<CartModel> = mutableListOf()
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve existing cart items from EasyPrefs
        val cartItemsJson = Prefs.getString("cartItemsList", cartItemsList.toString())
        cartItemsList = gson.fromJson(cartItemsJson, object : TypeToken<MutableList<CartModel>>() {}.type)
    }

    fun showToast(msg: String,context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun addCartItem(cartModel: CartModel) {
        if (!cartItemsList.contains(cartModel)) {
            // Add the new cart item
            cartItemsList.add(cartModel)
        } else {
            Log.d("onCartError", "${cartModel.name} has already been added to the list!")
        }

        // Convert the updated list to JSON string
        val updatedCartItemsJson = gson.toJson(cartItemsList)

        // Store the updated list in EasyPrefs
        Prefs.putString("cartItemsList", updatedCartItemsJson)
    }

    fun getCartItems(): MutableList<CartModel> {
        return cartItemsList
    }
}