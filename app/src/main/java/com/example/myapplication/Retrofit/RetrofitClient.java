package com.example.myapplication.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.0.85:8080";
    //local url

    public static IMyService getApiService() {
        return getInstance().create(IMyService.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

//    private static Retrofit instance;
//    public static Retrofit getInstance(){
//        if (instance == null){
//            instance = new Retrofit.Builder()
//                    .baseUrl("http://10.0.2.2:3000")
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build();
//        }
//        return instance;
//    }
}
