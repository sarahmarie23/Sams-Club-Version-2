package com.example.samsversion2

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.samsversion2.data.database.AppDatabase
import com.example.samsversion2.data.model.Item
import com.example.samsversion2.data.repository.ItemRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

@HiltAndroidApp
class SamsApplication : Application() {
    private val database by lazy { AppDatabase.getInstance(this) }
    private val itemRepository by lazy { ItemRepository(database.itemDao()) }
    private val sharedPrefs by lazy { getSharedPreferences("app_prefs", Context.MODE_PRIVATE)}

    override fun onCreate() {
        super.onCreate()
        initializeDataIfNeeded()
    }
    private fun initializeDataIfNeeded() {
        val isDataInitialized = sharedPrefs.getBoolean("data_initialized", false)
        if (!isDataInitialized) {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("SamsApplication", "onCreate: Initializing data")
                itemRepository.initializeData()
                sharedPrefs.edit().putBoolean("data_initialized", true).apply()
            }
        }
    }
}