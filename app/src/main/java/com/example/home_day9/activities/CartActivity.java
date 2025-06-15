package com.example.home_day9.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.example.home_day9.R;
import com.example.home_day9.adapters.CartAdapter;
import com.example.home_day9.database.AppDatabase;
import com.example.home_day9.database.CartItem;
import com.example.home_day9.database.CartRepository;
import com.example.home_day9.viewmodels.CartViewModel;
import com.example.home_day9.viewmodels.CartViewModelFactory;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemActionListener {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView textViewTotalPrice;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

        cartAdapter = new CartAdapter(this, this);
        recyclerViewCart.setAdapter(cartAdapter);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase database = AppDatabase.getDatabase(getApplication());
        CartRepository repository = new CartRepository(database.cartItemDao());
        // Sử dụng application context cho AndroidViewModelFactory
        cartViewModel = new ViewModelProvider(this, new CartViewModelFactory(getApplication(), repository)).get(CartViewModel.class);

        cartViewModel.getAllCartItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cartAdapter.setCartItems(cartItems);
                calculateTotalPrice(cartItems);
            }
        });

        setupSwipeToDelete();
    }

    private void calculateTotalPrice(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getProductPrice() * item.getQuantity();
        }
        textViewTotalPrice.setText(String.format(Locale.US, "Total: $ %.2f", total));
    }

    @Override
    public void onIncreaseQuantity(CartItem cartItem) {
        cartViewModel.updateCartItemQuantity(cartItem, cartItem.getQuantity() + 1);
    }

    @Override
    public void onDecreaseQuantity(CartItem cartItem) {
        cartViewModel.updateCartItemQuantity(cartItem, cartItem.getQuantity() - 1);
    }

    @Override
    public void onDeleteItem(CartItem cartItem) {
        cartViewModel.deleteCartItem(cartItem);
        Toast.makeText(this, cartItem.getProductName() + " removed from cart", Toast.LENGTH_SHORT).show();
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0, // Không hỗ trợ kéo thả
                ItemTouchHelper.LEFT // Vuốt từ phải sang trái để xóa
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartItem itemToDelete = cartAdapter.getCartItems().get(position);
                onDeleteItem(itemToDelete);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                ColorDrawable background = new ColorDrawable(Color.RED);
                // Sử dụng Android's built-in delete icon hoặc icon tùy chỉnh của bạn
                android.graphics.drawable.Drawable deleteIcon = ContextCompat.getDrawable(CartActivity.this, android.R.drawable.ic_menu_delete);

                if (deleteIcon == null) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    return;
                }

                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                if (dX < 0) { // Vuốt sang trái
                    int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    background.setBounds(
                            itemView.getRight() + (int) dX,
                            itemView.getTop(),
                            itemView.getRight(),
                            itemView.getBottom()
                    );
                } else { // Không làm gì khi vuốt sang phải
                    background.setBounds(0, 0, 0, 0);
                    deleteIcon.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                deleteIcon.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewCart);
    }
}