package com.example.snacksprint.cart.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable



@Serializable
data class CartModel(
    val imageUrl: String,
    val name: String,
    val price: Double,
    val units: String
)
