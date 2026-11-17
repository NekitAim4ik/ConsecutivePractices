package com.practicum.practice3.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.practicum.practice3.domain.model.ProductFilters
import com.practicum.practice3.domain.repository.FiltersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class FiltersRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : FiltersRepository {

    override val filters: Flow<ProductFilters> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            ProductFilters(
                category = prefs[CATEGORY],
                query = prefs[QUERY].orEmpty(),
                minNutriscore = prefs[MIN_NUTRISCORE]
            )
        }

    override suspend fun save(filters: ProductFilters) {
        dataStore.edit { prefs ->
            prefs.setOrRemove(CATEGORY, filters.category)
            prefs.setOrRemove(QUERY, filters.query.trim().takeIf { it.isNotEmpty() })
            prefs.setOrRemove(MIN_NUTRISCORE, filters.minNutriscore)
        }
    }

    private fun <T> MutablePreferences.setOrRemove(key: Preferences.Key<T>, value: T?) {
        if (value == null) remove(key) else this[key] = value
    }

    private companion object {
        val CATEGORY = stringPreferencesKey("category")
        val QUERY = stringPreferencesKey("query")
        val MIN_NUTRISCORE = stringPreferencesKey("min_nutriscore")
    }
}