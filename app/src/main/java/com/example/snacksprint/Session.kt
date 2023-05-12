package com.example.snacksprint

import com.example.snacksprint.cart.model.CartModel
import com.google.gson.Gson

class Session {
    var cartItemsList: MutableList<CartModel> = ArrayList()
    private var cartModelList: MutableList<CartModel> = mutableListOf()
    private val gson = Gson()
}