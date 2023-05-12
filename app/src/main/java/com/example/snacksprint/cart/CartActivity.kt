package com.example.snacksprint.cart


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snacksprint.BaseActivity
import com.example.snacksprint.R
import com.example.snacksprint.cart.adapter.CartAdapter
import com.example.snacksprint.cart.adapter.CartItemsAdapter
import com.example.snacksprint.cart.model.CartModel
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat

class CartActivity : BaseActivity() {
    lateinit var cartItemsAdapter: CartItemsAdapter
    lateinit var recyclerAdapter: CartAdapter //call the adapter
    lateinit var progressbar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var tvTotal: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        recyclerView = findViewById(R.id.rvCartItems)
        tvTotal = findViewById(R.id.tvTotal)

        cartItemsAdapter = CartItemsAdapter(cartList = getCartItems().toList(), this)

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = cartItemsAdapter
        val value = getCartValue(getCartItems().toList())
        tvTotal.text = "KES $value"

    }

    private fun getCartValue(cartList: List<CartModel>): String{
        var totalValue = 0.0
        for (cartModel in cartList) {
            val itemValue = cartModel.price.toInt() * cartModel.units.toInt()
            totalValue += itemValue
        }
        return formatNumber(totalValue.toInt())
    }

    private fun formatNumber(price: Int): String{
        val formPrice = price/10
        var fmamo = "0.00"
        try {
            val amou = formPrice.toDouble()
            if (amou > 0) {
                val df = DecimalFormat("#,###.00")
                fmamo = df.format(amou)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return fmamo
    }

}