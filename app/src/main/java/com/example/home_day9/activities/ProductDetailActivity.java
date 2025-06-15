package com.example.home_day9.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.home_day9.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.home_day9.R;
import com.example.home_day9.api.RetrofitClient;
import com.example.home_day9.database.AppDatabase;
import com.example.home_day9.database.CartItem;
import com.example.home_day9.database.CartRepository;
import com.example.home_day9.models.Product;
import com.example.home_day9.viewmodels.CartViewModel;
import com.example.home_day9.viewmodels.CartViewModelFactory;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imageViewDetailProduct;
    private TextView textViewDetailProductName;
    private TextView textViewDetailProductDescription;
    private TextView textViewDetailProductPrice;
    private TextView textViewDetailProductSize;
    private TextView textViewDetailProductColor;
    private Button buttonAddToCart;

    private Product currentProduct;
    private CartViewModel cartViewModel; // Thêm ViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imageViewDetailProduct = findViewById(R.id.imageViewDetailProduct);
        textViewDetailProductName = findViewById(R.id.textViewDetailProductName);
        textViewDetailProductDescription = findViewById(R.id.textViewDetailProductDescription);
        textViewDetailProductPrice = findViewById(R.id.textViewDetailProductPrice);
        textViewDetailProductSize = findViewById(R.id.textViewDetailProductSize);
        textViewDetailProductColor = findViewById(R.id.textViewDetailProductColor);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);

        // Khởi tạo ViewModel cho giỏ hàng
        AppDatabase database = AppDatabase.getDatabase(getApplication());
        CartRepository repository = new CartRepository(database.cartItemDao());
        cartViewModel = new ViewModelProvider(this, new CartViewModelFactory(repository)).get(CartViewModel.class);


        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            fetchProductDetail(productId);
        } else {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProduct != null) {
                    addToCart(currentProduct);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Product data not loaded yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchProductDetail(int productId) {
        RetrofitClient.getApiService().getProductDetail(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentProduct = response.body();
                    displayProductDetail(currentProduct);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to load details: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("ProductDetailActivity", "Response not successful: " + response.message());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ProductDetailActivity", "Error fetching product detail", t);
                finish();
            }
        });
    }

    private void displayProductDetail(Product product) {
        Glide.with(this)
                .load(product.getThumbnail())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageViewDetailProduct);
        textViewDetailProductName.setText(product.getTitle());
        textViewDetailProductDescription.setText(product.getDescription());
        textViewDetailProductPrice.setText(String.format(Locale.US, "$ %.2f", product.getPrice()));
        textViewDetailProductSize.setText("Stock: " + product.getStock());
        textViewDetailProductColor.setText("Brand: " + product.getBrand()); // Hoặc một trường khác nếu bạn có
    }

    private void addToCart(Product product) {
        // Sử dụng ViewModel để thêm vào giỏ hàng
        cartViewModel.addToCart(product);
        Toast.makeText(this, product.getTitle() + " added to cart!", Toast.LENGTH_SHORT).show();
    }
}