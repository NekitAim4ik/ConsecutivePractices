package com.practicum.practice3.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")