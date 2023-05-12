package com.example.snacksprint.network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClientString {

    public static final String BASE_URL = "http://www.thecocktaildb.com/api/json/v1/1/";
    //public static final String BASE_URL = "https://drinks-digital1.p.rapidapi.com";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /* builder.addInterceptor(chain -> {
            Request original = chain.request();
            //todo("hide this value before pushing code online")
            Request request = original.newBuilder()
                    .addHeader("X-RapidAPI-Key", "16ab568b93msh2ceac9967a22025p1e6d0cjsnf11ce166287e")
                    .addHeader("X-RapidAPI-Host", "drinks-digital1.p.rapidapi.com")
                    .build();
            return chain.proceed(request);
        });*/
        int KEY_TIMEOUT = 120;
        builder.readTimeout(KEY_TIMEOUT, TimeUnit.SECONDS).connectTimeout(KEY_TIMEOUT, TimeUnit.SECONDS).writeTimeout(KEY_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        OkHttpClient okHttpClient = builder.build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }
}
