package com.example.samsversion2.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.samsversion2.R
import com.example.samsversion2.data.model.Item
import com.example.samsversion2.data.database.ItemDao
import com.example.samsversion2.data.model.ListEntity
import com.example.samsversion2.data.model.ListItemCrossRef

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: LiveData<List<Item>> = itemDao.getAllItems()

    fun getItemsForList(listId: Long): LiveData<List<Item>> {
        Log.d("itemrepo", "getitemsforlist is being triggered list id $listId")

        return itemDao.getItemsForList(listId)
    }

    suspend fun initializeData() {
        var defaultListId = getDefaultListId()
        if (defaultListId == null) {
            Log.d("itemrepo", "No default list found, inserting new list")
            defaultListId = itemDao.insertList(ListEntity(listName = "Default list"))
            Log.d("itemrepo", "Created new list with ID: $defaultListId")
        }
        Log.d("itemrepo", "initialize is being triggered list id $defaultListId")
        val items = listOf(
            Item(
                itemName = "Avocados",
                itemNumber = "622943",
                imageUrl = R.drawable.avocados
            ),
            Item(
                itemName = "Bananas",
                itemNumber = "362153",
                imageUrl = R.drawable.bananas
            ),
            Item(
                itemName = "Blackberries",
                itemNumber = "284479",
                imageUrl = R.drawable.blackberries
            ),
            Item(
                itemName = "Blueberries",
                itemNumber = "279457 ",
                imageUrl = R.drawable.blueberries
            ),
            Item(
                itemName = "Cantaloupe",
                itemNumber = "867552",
                imageUrl = R.drawable.cantaloupe
            ),
            Item(
                itemName = "Cara cara oranges",
                itemNumber = "967219",
                imageUrl = R.drawable.caracaraoranges
            ),
            Item(
                itemName = "Clementine mandarins",
                itemNumber = "457334",
                imageUrl = R.drawable.clementines
            ),
            Item(
                itemName = "Green grapes",
                itemNumber = "725545",
                imageUrl = R.drawable.greengrapes
            ),
            Item(
                itemName = "Navel oranges",
                itemNumber = "980167341",
                imageUrl = R.drawable.redgrapes
            ),
            Item(
                itemName = "Organic bananas",
                itemNumber = "105832",
                imageUrl = R.drawable.organicbananas
            ),
            Item(
                itemName = "Organic Gala Apples",
                itemNumber = "980319308",
                imageUrl = R.drawable.organicgalaapples
            ),
            Item(
                itemName = "Pineapple",
                itemNumber = "835891",
                imageUrl = R.drawable.pineapple
            ),
            Item(
                itemName = "Raspberries",
                itemNumber = "33249",
                imageUrl = R.drawable.raspberries
            ),
            Item(
                itemName = "Red grapes",
                itemNumber = "72553",
                imageUrl = R.drawable.redgrapes
            ),
            Item(
                itemName = "Strawberries",
                itemNumber = "749972",
                imageUrl = R.drawable.strawberries
            ),
            Item(
                itemName = "Watermelon",
                itemNumber = "825216",
                imageUrl = R.drawable.watermelon
            ),
            Item(
                itemName = "Farmer John Bacon ",
                itemNumber = "980154155",
                imageUrl = R.drawable.bacon
            ),
            Item(
                itemName = "Babybel Original",
                itemNumber = "980156320",
                imageUrl = R.drawable.babybellight
            ),
            Item(
                itemName = "Babybel Light",
                itemNumber = "980157831",
                imageUrl = R.drawable.original
            ),
            Item(
                itemName = "Ice (20 lbs)",
                itemNumber = "693656",
                imageUrl = R.drawable.ice
            )
        )
        Log.d("DatabaseInit", "Inserting items into the database")

        itemDao.insertAll(items)
        val insertedItemIds = itemDao.insertItemsFromList(items)
        insertedItemIds.forEachIndexed { index, itemId ->
            itemDao.insertListItemCrossRef(
                ListItemCrossRef(listId = defaultListId, itemId = itemId)
            )
        }
    }

    private suspend fun getDefaultListId(): Long? {
        return itemDao.getListIdByName("Default list")
    }

    suspend fun ensureDefaultListExists() {
        val defaultListId = getDefaultListId()
        if (defaultListId == null) {
            Log.d("itemrepo", "Default list does not exist. Inserting items...")
            initializeData()
        } else {
            val itemCount = itemDao.getItemCountForList(defaultListId)
            if (itemCount == 0) {
                Log.d("itemrepo", "Default list exists but is empty. Inserting items...")
                initializeData()
            } else {
                Log.d("itemrepo", "Default list exists with ID: $defaultListId and contains $itemCount items.")
            }
        }
    }

    suspend fun insertItem(item: Item): Long {
        return itemDao.insert(item)
    }

    suspend fun insertItemToList(listId: Long, itemId: Long) {
        itemDao.insertListItemCrossRef(ListItemCrossRef(listId = listId, itemId = itemId))
    }

    suspend fun deleteItem(itemId: Long) {
        itemDao.deleteItem(itemId)
        itemDao.deleteCrossRefWithItemId(itemId)
    }

    suspend fun removeItemFromList(listId: Long, itemId: Long) {
        itemDao.deleteCrossRef(listId, itemId)
    }
}
