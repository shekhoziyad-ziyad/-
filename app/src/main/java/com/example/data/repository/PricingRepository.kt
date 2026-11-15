package com.example.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.pricingDataStore: DataStore<Preferences> by preferencesDataStore(name = "boq_pricing_prefs")

data class StoredPricingState(
    val materialPrices: Map<String, Double> = emptyMap(),
    val lineOverrides: Map<String, Double> = emptyMap(),
    val profitRate: Double = 0.15
)

class PricingRepository(private val context: Context) {

    private companion object {
        const val MAT_PREFIX = "mat_price_"
        const val LINE_PREFIX = "line_override_"
        val PROFIT_RATE_KEY = doublePreferencesKey("profit_rate")
    }

    val storedStateFlow: Flow<StoredPricingState> = context.pricingDataStore.data.map { prefs ->
        val mats = mutableMapOf<String, Double>()
        val lines = mutableMapOf<String, Double>()
        var profit = 0.15

        prefs.asMap().forEach { (key, value) ->
            val name = key.name
            if (value is Double) {
                when {
                    name.startsWith(MAT_PREFIX) -> mats[name.removePrefix(MAT_PREFIX)] = value
                    name.startsWith(LINE_PREFIX) -> lines[name.removePrefix(LINE_PREFIX)] = value
                    name == PROFIT_RATE_KEY.name -> profit = value
                }
            }
        }
        StoredPricingState(
            materialPrices = mats,
            lineOverrides = lines,
            profitRate = profit
        )
    }

    suspend fun saveMaterialPrice(materialId: String, price: Double?) {
        val key = doublePreferencesKey("$MAT_PREFIX$materialId")
        context.pricingDataStore.edit { prefs ->
            if (price == null) {
                prefs.remove(key)
            } else {
                prefs[key] = price
            }
        }
    }

    suspend fun saveLineOverride(lineId: String, price: Double?) {
        val key = doublePreferencesKey("$LINE_PREFIX$lineId")
        context.pricingDataStore.edit { prefs ->
            if (price == null) {
                prefs.remove(key)
            } else {
                prefs[key] = price
            }
        }
    }

    suspend fun saveProfitRate(rate: Double) {
        context.pricingDataStore.edit { prefs ->
            prefs[PROFIT_RATE_KEY] = rate
        }
    }

    suspend fun resetAll() {
        context.pricingDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
