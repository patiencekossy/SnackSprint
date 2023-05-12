package com.example.snacksprint.cart.data

import androidx.datastore.core.Serializer
import com.example.snacksprint.cart.model.CartItems
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object CartItemsSerializer: Serializer<CartItems> {
    override val defaultValue: CartItems
        get() = CartItems()

    override suspend fun readFrom(input: InputStream): CartItems {
        return try {
            Json.decodeFromString(
                deserializer = CartItems.serializer(),
                string = input.readBytes().decodeToString()
            )
        }catch (e: SerializationException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: CartItems, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = CartItems.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}