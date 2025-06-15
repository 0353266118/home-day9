package com.example.home_day9.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.home_day9.R;
import com.example.home_day9.database.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems = new ArrayList<>();
    private Context context;
    private OnCartItemActionListener listener;

    public interface OnCartItemActionListener {
        void onIncreaseQuantity(CartItem cartItem);
        void onDecreaseQuantity(CartItem cartItem);
        void onDeleteItem(CartItem cartItem); // Dùng cho vuốt để xóa
    }

    public CartAdapter(Context context, OnCartItemActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCartItem;
        TextView textViewCartItemName;
        TextView textViewCartItemPrice;
        TextView textViewCartItemQuantity;
        TextView textViewCartItemTotalPrice;
        Button buttonDecreaseQuantity;
        Button buttonIncreaseQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCartItem = itemView.findViewById(R.id.imageViewCartItem);
            textViewCartItemName = itemView.findViewById(R.id.textViewCartItemName);
            textViewCartItemPrice = itemView.findViewById(R.id.textViewCartItemPrice);
            textViewCartItemQuantity = itemView.findViewById(R.id.textViewCartItemQuantity);
            textViewCartItemTotalPrice = itemView.findViewById(R.id.textViewCartItemTotalPrice);
            buttonDecreaseQuantity = itemView.findViewById(R.id.buttonDecreaseQuantity);
            buttonIncreaseQuantity = itemView.findViewById(R.id.buttonIncreaseQuantity);

            buttonIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onIncreaseQuantity(cartItems.get(getAdapterPosition()));
                    }
                }
            });

            buttonDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onDecreaseQuantity(cartItems.get(getAdapterPosition()));
                    }
                }
            });

            // Nhấn giữ để tăng số lượng sản phẩm muốn mua
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onIncreaseQuantity(cartItems.get(getAdapterPosition()));
                        return true; // Tiêu thụ sự kiện long click
                    }
                    return false;
                }
            });
        }

        public void bind(CartItem cartItem) {
            Glide.with(context)
                    .load(cartItem.getProductThumbnail())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageViewCartItem);
            textViewCartItemName.setText(cartItem.getProductName());
            textViewCartItemPrice.setText(String.format(Locale.US, "$ %.2f", cartItem.getProductPrice()));
            textViewCartItemQuantity.setText(String.valueOf(cartItem.getQuantity()));
            textViewCartItemTotalPrice.setText(String.format(Locale.US, "$ %.2f", cartItem.getProductPrice() * cartItem.getQuantity()));
        }
    }
}
