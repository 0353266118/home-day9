package com.example.home_day9.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCartItem(CartItem cartItem);

    @Update
    void updateCartItem(CartItem cartItem);

    @Delete
    void deleteCartItem(CartItem cartItem);

    @Query("SELECT * FROM cart_items")
    LiveData<List<CartItem>> getAllCartItems(); // Sử dụng LiveData

    @Query("SELECT * FROM cart_items WHERE product_id = :productId LIMIT 1")
    CartItem getCartItemByProductId(int productId);
}
