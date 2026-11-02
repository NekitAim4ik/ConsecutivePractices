package com.practicum.practice3.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.practice3.di.NetworkModule
import com.practicum.practice3.storage.ProductRepository

class ProductListViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            return ProductListViewModel(NetworkModule.getProductsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProductDetailsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(NetworkModule.getProductByCodeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}