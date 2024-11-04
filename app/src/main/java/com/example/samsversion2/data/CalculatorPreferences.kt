package com.example.samsversion2.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("calculator_prefs")

object CalculatorPreferences {
    private val LAST_COST = doublePreferencesKey("last_cost")

    fun getLastCost(context: Context): Flow<Double?> {
        return context.dataStore.data.map { preferences ->
            preferences[LAST_COST]
        }
    }

    suspend fun saveLastCost(context: Context, cost: Double) {
        context.dataStore.edit { preferences ->
            preferences[LAST_COST] = cost
        }
    }
}
