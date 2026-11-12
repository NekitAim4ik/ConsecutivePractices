package com.practicum.practice3.di

import com.practicum.practice3.data.local.db.AppDatabase
import com.practicum.practice3.data.local.prefs.filtersDataStore
import com.practicum.practice3.data.local.prefs.profileDataStore
import com.practicum.practice3.data.remote.NetworkClient
import com.practicum.practice3.data.repository.FavoritesRepositoryImpl
import com.practicum.practice3.data.repository.FiltersRepositoryImpl
import com.practicum.practice3.data.repository.ProductRepositoryImpl
import com.practicum.practice3.data.repository.ProfileRepositoryImpl
import com.practicum.practice3.data.repository.ResumeRepositoryImpl
import com.practicum.practice3.domain.cache.FilterBadgeCache
import com.practicum.practice3.domain.repository.FavoritesRepository
import com.practicum.practice3.domain.repository.FiltersRepository
import com.practicum.practice3.domain.repository.ProductRepository
import com.practicum.practice3.domain.repository.ProfileRepository
import com.practicum.practice3.domain.repository.ResumeRepository
import com.practicum.practice3.domain.usecase.DownloadResumeUseCase
import com.practicum.practice3.domain.usecase.GetProductByCodeUseCase
import com.practicum.practice3.domain.usecase.GetProductsUseCase
import com.practicum.practice3.viewmodel.EditProfileViewModel
import com.practicum.practice3.viewmodel.FavoritesViewModel
import com.practicum.practice3.viewmodel.FiltersViewModel
import com.practicum.practice3.viewmodel.ProductDetailsViewModel
import com.practicum.practice3.viewmodel.ProductListViewModel
import com.practicum.practice3.viewmodel.ProfileViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {

    single { NetworkClient.api }

    single { AppDatabase.create(androidContext()) }
    single { get<AppDatabase>().favoriteProductDao() }

    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    single<FiltersRepository> { FiltersRepositoryImpl(androidContext().filtersDataStore) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }

    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            androidContext(),
            androidContext().profileDataStore
        )
    }
    single<ResumeRepository> { ResumeRepositoryImpl(androidContext(), get()) }

    single { FilterBadgeCache() }

    factory { GetProductsUseCase(get()) }
    factory { GetProductByCodeUseCase(get()) }
    factory { DownloadResumeUseCase(get()) }

    viewModel { ProductListViewModel(get(), get(), get()) }
    viewModel { ProductDetailsViewModel(get(), get()) }
    viewModel { FiltersViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get()) }
}