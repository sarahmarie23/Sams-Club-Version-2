package com.example.samsversion2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samsversion2.data.model.Item
import com.example.samsversion2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.ensureDefaultListExists()
        }
    }

    val defaultListItems: LiveData<List<Item>> = repository.getItemsForList(1).also {
        it.observeForever { items ->
            if (items != null) {
                Log.d("ListViewModel", "Fetched ${items.size} items for default list.")
            }
        }
    }

    val allItems: LiveData<List<Item>> = repository.allItems.also {
        it.observeForever { items ->
            Log.d("ListViewModel", "Fetched ${items.size} items in total.")
        }
    }
    /* fix later
        fun addItem(item: Item) = viewModelScope.launch {
            Log.d("ListViewModel", "Inserting item: ${item.itemName}")
            repository.insertItem(item)
        }
*/
    fun deleteItem(itemId: Long) = viewModelScope.launch {
        Log.d("ListViewModel", "Deleting item: ${itemId}")
        repository.deleteItem(itemId)
    }

    fun addItemToDefaultList(itemNumber: String, itemName: String, imageUrl: String, isDownloaded: Boolean) {
        viewModelScope.launch {
            val item = Item(
                itemId = 0, // Auto-generated
                itemName = itemName,
                itemNumber = itemNumber,
                imagePath = imageUrl,
                isDownloaded = true
            )
            // Insert item into the repository
            val insertedItemId = repository.insertItem(item)

            // Assuming 1 is the ID of the Default List
            repository.insertItemToList(listId = 1, itemId = insertedItemId)
        }
    }

    suspend fun doesItemExist(itemNumber: String): Boolean {
        return repository.getItemCount(itemNumber) > 0
    }
}
