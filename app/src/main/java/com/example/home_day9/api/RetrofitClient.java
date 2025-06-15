package com.example.home_day9.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://dummyjson.com/";
    private static ProductApiService apiService;

    public static ProductApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ProductApiService.class);
        }
        return apiService;
    }
}