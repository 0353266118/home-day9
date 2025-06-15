package com.example.home_day9.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.home_day9.R; // Đảm bảo đúng package R
import com.example.home_day9.models.Product;

import java.util.List;
import java.util.Locale; // Để định dạng tiền tệ

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> products, OnItemClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(products.get(getAdapterPosition()));
                    }
                }
            });
        }

        public void bind(Product product) {
            textViewProductName.setText(product.getTitle());
            textViewProductPrice.setText(String.format(Locale.US, "$ %.2f", product.getPrice()));
            Glide.with(context)
                    .load(product.getThumbnail())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageViewProduct);
        }
    }
}