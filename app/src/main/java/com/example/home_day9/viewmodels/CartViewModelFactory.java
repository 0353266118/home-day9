package com.example.home_day9.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.home_day9.database.CartRepository;

public class CartViewModelFactory implements ViewModelProvider.Factory {
    private final Application application; // Nếu dùng AndroidViewModel
    private final CartRepository repository;

    // Constructor nếu CartViewModel extends AndroidViewModel
    public CartViewModelFactory(@NonNull Application application, CartRepository repository) {
        this.application = application;
        this.repository = repository;
    }

    // Constructor nếu CartViewModel chỉ extends ViewModel
    public CartViewModelFactory(CartRepository repository) {
        this.application = null; // Không dùng Application
        this.repository = repository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            if (application != null) { // Nếu CartViewModel là AndroidViewModel
                return (T) new CartViewModel(application, repository);
            } else { // Nếu CartViewModel chỉ là ViewModel
                return (T) new CartViewModel(repository);
            }
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}