package com.example.home_day9.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData; // Nếu bạn muốn LiveData có thể thay đổi
import androidx.annotation.NonNull;

import com.example.home_day9.database.CartItem;
import com.example.home_day9.database.CartRepository;
import com.example.home_day9.models.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartViewModel extends AndroidViewModel { // Hoặc ViewModel nếu không cần Context

    private CartRepository repository;
    private LiveData<List<CartItem>> allCartItems;
    private final ExecutorService backgroundExecutor; // Dùng để thực hiện các thao tác DB

    public CartViewModel(@NonNull Application application, CartRepository repository) {
        super(application);
        this.repository = repository;
        this.allCartItems = repository.getAllCartItems();
        this.backgroundExecutor = Executors.newSingleThreadExecutor(); // Một thread riêng cho các thao tác ViewModel
    }

    // Constructor cho ViewModel (nếu không extends AndroidViewModel)
    public CartViewModel(CartRepository repository) {
        super(null); // Hoặc bạn cần cung cấp Application context nếu cần
        this.repository = repository;
        this.allCartItems = repository.getAllCartItems();
        this.backgroundExecutor = Executors.newSingleThreadExecutor();
    }


    public LiveData<List<CartItem>> getAllCartItems() {
        return allCartItems;
    }

    public void addToCart(Product product) {
        backgroundExecutor.execute(() -> {
            CartItem existingItem = repository.getCartItemByProductId(product.getId());
            if (existingItem != null) {
                // Sản phẩm đã có trong giỏ, tăng số lượng
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                repository.update(existingItem);
            } else {
                // Thêm sản phẩm mới vào giỏ
                CartItem newItem = new CartItem(
                        product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getThumbnail(),
                        1
                );
                repository.insert(newItem);
            }
        });
    }

    public void updateCartItemQuantity(CartItem cartItem, int newQuantity) {
        backgroundExecutor.execute(() -> {
            if (newQuantity > 0) {
                cartItem.setQuantity(newQuantity);
                repository.update(cartItem);
            } else {
                // Nếu số lượng là 0, xóa khỏi giỏ hàng
                repository.delete(cartItem);
            }
        });
    }

    public void deleteCartItem(CartItem cartItem) {
        backgroundExecutor.execute(() -> repository.delete(cartItem));
    }
}

