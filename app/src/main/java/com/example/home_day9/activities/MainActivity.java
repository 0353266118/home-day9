package com.example.home_day9.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.home_day9.R;
import com.example.home_day9.adapters.ProductAdapter;
import com.example.home_day9.api.RetrofitClient;
import com.example.home_day9.models.Product;
import com.example.home_day9.models.ProductResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2));

        productAdapter = new ProductAdapter(this, productList, this);
        recyclerViewProducts.setAdapter(productAdapter);

        fetchProducts();
    }

    private void fetchProducts() {
        RetrofitClient.getApiService().getProducts().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body().getProducts());
                    productAdapter.setProducts(productList); // Cập nhật adapter
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load products: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error fetching products", t);
            }
        });
    }

    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }
}