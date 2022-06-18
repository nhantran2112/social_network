package com.example.blog_javacode.Retrofit2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    public  static Retrofit getClient(String baseurl){
        OkHttpClient builder = new OkHttpClient.Builder().readTimeout(50000, TimeUnit.MILLISECONDS)
                                                                .writeTimeout(50000 , TimeUnit.MILLISECONDS)
                                                                .connectTimeout(100000 , TimeUnit.MILLISECONDS)
                                                                .retryOnConnectionFailure(true)
                                                                .build();
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder().baseUrl(baseurl).client(builder).addConverterFactory(GsonConverterFactory.create(gson)).build();

    }
}
