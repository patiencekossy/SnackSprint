package com.example.snacksprint.cart.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class CartItems(
    val cartItem: PersistentList<CartModel> = persistentListOf()
)
