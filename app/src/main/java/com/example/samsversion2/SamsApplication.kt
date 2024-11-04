package com.example.samsversion2

import android.app.Application
import com.example.samsversion2.data.database.AppDatabase
import com.example.samsversion2.data.model.Item
import com.example.samsversion2.data.repository.ItemRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class SamsApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { ItemRepository(database.itemDao()) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            if (repository.isEmpty()) {
                val sampleItems = listOf(
                    Item(id = 1, itemName = "Sample Item 1", itemNumber = "001", imageUrl = R.drawable.avocados),
                    Item(id = 2, itemName = "Sample Item 2", itemNumber = "002", imageUrl = R.drawable.bananas),

                )
                repository.insertAll(sampleItems) // Insert initial items
            }
        }
    }
}