package com.practicum.practice3.di

import com.practicum.practice3.data.local.db.AppDatabase
import com.practicum.practice3.data.local.prefs.filtersDataStore
import com.practicum.practice3.data.remote.NetworkClient
import com.practicum.practice3.data.repository.FavoritesRepositoryImpl
import com.practicum.practice3.data.repository.FiltersRepositoryImpl
import com.practicum.practice3.data.repository.ProductRepositoryImpl
import com.practicum.practice3.domain.cache.FilterBadgeCache
import com.practicum.practice3.domain.repository.FavoritesRepository
import com.practicum.practice3.domain.repository.FiltersRepository
import com.practicum.practice3.domain.repository.ProductRepository
import com.practicum.practice3.domain.usecase.GetProductByCodeUseCase
import com.practicum.practice3.domain.usecase.GetProductsUseCase
import com.practicum.practice3.viewmodel.FavoritesViewModel
import com.practicum.practice3.viewmodel.FiltersViewModel
import com.practicum.practice3.viewmodel.ProductDetailsViewModel
import com.practicum.practice3.viewmodel.ProductListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { NetworkClient.api }

    single { AppDatabase.create(androidContext()) }
    single { get<AppDatabase>().favoriteProductDao() }

    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    single<FiltersRepository> { FiltersRepositoryImpl(androidContext().filtersDataStore) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }

    single { FilterBadgeCache() }

    factory { GetProductsUseCase(get()) }
    factory { GetProductByCodeUseCase(get()) }

    viewModel { ProductListViewModel(get(), get(), get()) }
    viewModel { ProductDetailsViewModel(get(), get()) }
    viewModel { FiltersViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
}