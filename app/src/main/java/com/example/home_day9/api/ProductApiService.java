package com.example.home_day9.api;

import com.example.home_day9.models.Product;
import com.example.home_day9.models.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApiService {
    @GET("products")
    Call<ProductResponse> getProducts();

    @GET("products/{id}")
    Call<Product> getProductDetail(@Path("id") int productId);
}