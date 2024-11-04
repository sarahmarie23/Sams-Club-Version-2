package com.example.samsversion2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
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

    val allItems: LiveData<List<Item>> = repository.allItems.asLiveData()

    fun addItem(item: Item) = viewModelScope.launch {
        repository.insertItem(item)
    }

    fun deleteItem(item: Item) = viewModelScope.launch {
        repository.deleteItem(item)
    }

    companion object {
        fun provideFactory(
            repository: ItemRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
                    return ListViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

    }
}
