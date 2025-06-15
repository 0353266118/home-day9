package com.example.home_day9.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "product_id")
    private int productId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "product_price")
    private double productPrice;

    @ColumnInfo(name = "product_thumbnail")
    private String productThumbnail;

    @ColumnInfo(name = "quantity")
    private int quantity;

    // Constructor
    public CartItem(int productId, String productName, double productPrice, String productThumbnail, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productThumbnail = productThumbnail;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; } // Set id khi đọc từ DB

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getProductPrice() { return productPrice; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }

    public String getProductThumbnail() { return productThumbnail; }
    public void setProductThumbnail(String productThumbnail) { this.productThumbnail = productThumbnail; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
