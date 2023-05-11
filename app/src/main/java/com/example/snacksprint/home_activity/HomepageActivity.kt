package com.example.snacksprint.home_activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.snacksprint.R
import com.example.snacksprint.home_activity.adapter.CategoryAdapter
import com.example.snacksprint.home_activity.adapter.DrinkAdapter
import com.example.snacksprint.home_activity.model.CategoryModel
import com.example.snacksprint.home_activity.model.Drink
import com.example.snacksprint.network.ApiClientString
import com.example.snacksprint.network.ApiInterface
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomepageActivity : AppCompatActivity() {
    //declare variables
    lateinit var drinksAdapter: DrinkAdapter
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var rvCat: RecyclerView
    lateinit var rvPopular: RecyclerView
    var categoryList: MutableList<CategoryModel> = ArrayList()
    var drinksList: MutableList<Drink> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        //init variables
        rvCat = findViewById(R.id.rvCat)
        rvPopular = findViewById(R.id.rvPopular)
        fetchCategories()

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
                        rvCat.layoutManager = LinearLayoutManager(
                            this@HomepageActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        rvCat.adapter = categoryAdapter

                        //fetchPopularCocktails from Api
                        fetchPopularCocktails()

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


    private fun fetchPopularCocktails() {

        //todo showProgress
        val apiInterface: ApiInterface =
            ApiClientString.getClient().create(ApiInterface::class.java)
        val call: Call<String?>? = apiInterface.getDrinksPerCategoryList("Cocktail")
        call?.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                try {
                    Log.d("onResponse", response.body().toString())
                    val categoryItemList: MutableList<CategoryModel> = ArrayList()
                    if (response.isSuccessful) {

                        val jsonObj = JSONObject(response.body().toString())
                        val drinksArray = jsonObj.optJSONArray("drinks")
                        Log.d("onResponseObj", jsonObj.optJSONArray("drinks")[0].toString())
                        if (drinksArray != null) {
                            for (i in 0 until 10){
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



}



