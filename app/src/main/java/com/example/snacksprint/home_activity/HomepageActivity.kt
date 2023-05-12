package com.example.snacksprint.home_activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.dataStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.snacksprint.BaseActivity
import com.example.snacksprint.R
import com.example.snacksprint.SnackSprintApp
import com.example.snacksprint.cart.CartActivity
import com.example.snacksprint.cart.data.CartItemsSerializer
import com.example.snacksprint.cart.model.CartModel
import com.example.snacksprint.home_activity.adapter.CategoryAdapter
import com.example.snacksprint.home_activity.adapter.DrinkAdapter
import com.example.snacksprint.home_activity.model.CategoryModel
import com.example.snacksprint.home_activity.model.Drink
import com.example.snacksprint.home_activity.model.TagResponseModelItem
import com.example.snacksprint.network.ApiClientString
import com.example.snacksprint.network.ApiInterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomepageActivity : BaseActivity() {
    //declare variables
    lateinit var drinksAdapter: DrinkAdapter
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var rvCat: RecyclerView
    lateinit var rvPopular: RecyclerView
    lateinit var tvCategoryLabel: TextView
    lateinit var ivCart: ImageView
    var categoryList: MutableList<CategoryModel> = ArrayList()
    var drinksList: MutableList<Drink> = ArrayList()
    val Context.dataStore by dataStore("app-cart-items.json", CartItemsSerializer)
    // Define a coroutine scope
    val scope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        //init variables
        rvCat = findViewById(R.id.rvCat)
        rvPopular = findViewById(R.id.rvPopular)
        tvCategoryLabel = findViewById(R.id.tvCategoryLabel)
        ivCart = findViewById(R.id.ivCart)
        ivCart.setOnClickListener {
            startActivity(Intent(this@HomepageActivity,CartActivity::class.java))
        }

        fetchCategories()
        //fetchTags()

    }

    private fun fetchCategories() {
        //todo showProgress
        val apiInterface: ApiInterface =
            ApiClientString.getClient().create(ApiInterface::class.java)
        val call: Call<String?>? = apiInterface.getCategoryList("list")
        call?.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                try {
                    Log.d("onResponse", response.body().toString())
                    if (response.isSuccessful) {
                        val jsonObj = JSONObject(response.body().toString())
                        val categoryArray = jsonObj.optJSONArray("drinks")
                        Log.d("onResponseObj", jsonObj.optJSONArray("drinks")[0].toString())
                        if (categoryArray != null) {
                            for (i in 0 until categoryArray.length()){
                                val category = categoryArray.optJSONObject(i)
                                categoryList.add(CategoryModel(title = category.optString("strCategory")))
                                Log.d("onResponseArray", category.toString())
                            }
                        }
                        //initialise the recyclerView and adapter
                        categoryAdapter = CategoryAdapter(categoryList, this@HomepageActivity)
                        //populate adapter
                        rvCat.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL)
                        rvCat.adapter = categoryAdapter

                        categoryAdapter.setOnClickListener(object : CategoryAdapter.OnItemClickListener {
                            override fun onItemSelected(categoryModel: CategoryModel?, position: Int) {
                                tvCategoryLabel.text = categoryModel?.title ?: "Popular Cocktails"
                                categoryModel?.title?.let { fetchPopularCocktails(it) }
                            }

                        })
                        //fetchPopularCocktails from Api
                        fetchPopularCocktails("Cocktail")

                    }
                    else {
                        Toast.makeText(
                            this@HomepageActivity,
                            "Sorry something went wrong while processing your request",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Log.d("onResponseException", e.message.toString())
                }

            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Toast.makeText(
                    this@HomepageActivity,
                    "Sorry something went wrong while processing your request",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }


    private fun fetchPopularCocktails(filter: String) {
        //todo showProgress
        val apiInterface: ApiInterface =
            ApiClientString.getClient().create(ApiInterface::class.java)
        val call: Call<String?>? = apiInterface.getDrinksPerCategoryList(filter)
        call?.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                try {
                    Log.d("onResponse", response.body().toString())
                    val categoryItemList: MutableList<CategoryModel> = ArrayList()
                    if (response.isSuccessful) {
                        if (drinksList.size>0){
                            drinksList.clear()
                        }
                        val jsonObj = JSONObject(response.body().toString())
                        val drinksArray = jsonObj.optJSONArray("drinks")
                        Log.d("onResponseObj", jsonObj.optJSONArray("drinks")[0].toString())
                        if (drinksArray != null) {
                            for (i in 0 until drinksArray.length()){
                                val drink = drinksArray.optJSONObject(i)
                                drinksList.add(
                                    Drink(
                                        strDrink = drink.optString("strDrink"),
                                        strDrinkThumb = drink.optString("strDrinkThumb"),
                                        idDrink = drink.optString("idDrink")
                                    )
                                )
                                Log.d("onResponseArray", drink.toString())
                            }
                        }

                        //initialise the recyclerView and adapter
                        drinksAdapter = DrinkAdapter(drinksList, this@HomepageActivity)
                        //populate adapter
                        rvPopular.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                        rvPopular.adapter = drinksAdapter


                        drinksAdapter.setOnClickListener(object : DrinkAdapter.OnItemClickListener{
                            override fun onItemSelected(Drink: Drink?, position: Int) {

                                val cartItem = CartModel(
                                    name = Drink!!.strDrink,
                                    price = Drink.idDrink.toDouble(),
                                    units = "1",
                                    imageUrl = Drink.strDrinkThumb
                                )
                                addCartItem(cartItem)
                                Log.d("onAddedItems",getCartItems().toString())
                            }
                        })

                    } else {
                        Toast.makeText(
                            this@HomepageActivity,
                            "Sorry something went wrong while processing your request",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Log.d("onResponseException", e.message.toString())
                }

            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Toast.makeText(
                    this@HomepageActivity,
                    "Sorry something went wrong while processing your request",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }

    private fun fetchTags(){
        val apiInterface: ApiInterface = ApiClientString.getClient().create(ApiInterface::class.java)
        val call: Call<String?>? = apiInterface.getCocktailTags()
        call?.enqueue(object : Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                val json = response.body().toString()
                Log.d("onResponse",response.body().toString())
                val categories = Gson().fromJson<List<TagResponseModelItem>>(json, object : TypeToken<List<TagResponseModelItem>>() {}.type)
                Log.d("onResponse",categories[0].name)
                //initialise the recyclerView and adapter
                //categoryAdapter = CategoryAdapter(categories, this@HomepageActivity)
                //populate adapter
                /* rvCat.layoutManager = LinearLayoutManager(
                     this@HomepageActivity,
                     LinearLayoutManager.HORIZONTAL,
                     false
                 )*/
//                rvCat.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL)
//                rvCat.adapter = categoryAdapter
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.d("onFailure",t.message.toString())
                Toast.makeText(this@HomepageActivity,t.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        })

    }


}



