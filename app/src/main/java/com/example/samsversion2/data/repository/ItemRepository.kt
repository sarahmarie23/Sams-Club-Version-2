package com.example.samsversion2.data.repository

import androidx.lifecycle.LiveData
import com.example.samsversion2.data.Item
import com.example.samsversion2.data.ItemDao
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun isEmpty(): Boolean {
        return itemDao.getCount() == 0
    }

    suspend fun insertAll(items: List<Item>) {
        itemDao.insertAll(items)
    }

    suspend fun insertItem(item: Item) {
        itemDao.insertItem(item)
    }

    suspend fun deleteItem(item: Item) {
        itemDao.deleteItem(item)
    }
}
