package com.example.snacksprint.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

    interface ApiInterface {

        @GET("list.php")
        fun getCategoryList(@Query("c") category: String?): Call<String?>?

        @GET("filter.php")
        fun getDrinksPerCategoryList(@Query("c") category: String?): Call<String?>?

        @GET("/v1/tags")
        fun getCocktailTags(): Call<String?>?

        @GET("/v1/cocktails/tags")
        fun getCocktailByTagId(@Query("filters") tagId: String?): Call<String?>?

    }
