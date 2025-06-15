package com.example.home_day9.database;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {
    private CartItemDao cartItemDao;
    private LiveData<List<CartItem>> allCartItems;
    private final ExecutorService databaseWriteExecutor; // Để chạy các thao tác DB ở background

    public CartRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
        this.allCartItems = cartItemDao.getAllCartItems();
        databaseWriteExecutor = Executors.newFixedThreadPool(4); // Tạo một pool thread nhỏ
    }

    public LiveData<List<CartItem>> getAllCartItems() {
        return allCartItems;
    }

    public void insert(CartItem cartItem) {
        databaseWriteExecutor.execute(() -> cartItemDao.insertCartItem(cartItem));
    }

    public void update(CartItem cartItem) {
        databaseWriteExecutor.execute(() -> cartItemDao.updateCartItem(cartItem));
    }

    public void delete(CartItem cartItem) {
        databaseWriteExecutor.execute(() -> cartItemDao.deleteCartItem(cartItem));
    }

    public CartItem getCartItemByProductId(int productId) {

        return cartItemDao.getCartItemByProductId(productId);
    }
}