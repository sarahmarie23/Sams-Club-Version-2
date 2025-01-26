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
                imagePath = R.drawable.avocados.toString()
            ),
            Item(
                itemName = "Bananas",
                itemNumber = "362153",
                imagePath = R.drawable.bananas.toString()
            ),
            Item(
                itemName = "Blackberries",
                itemNumber = "284479",
                imagePath = R.drawable.blackberries.toString()
            ),
            Item(
                itemName = "Blueberries",
                itemNumber = "279457 ",
                imagePath = R.drawable.blueberries.toString()
            ),
            Item(
                itemName = "Cantaloupe",
                itemNumber = "867552",
                imagePath = R.drawable.cantaloupe.toString()
            ),
            Item(
                itemName = "Cara cara oranges",
                itemNumber = "967219",
                imagePath = R.drawable.caracaraoranges.toString()
            ),
            Item(
                itemName = "Clementine mandarins",
                itemNumber = "457334",
                imagePath = R.drawable.clementines.toString()
            ),
            Item(
                itemName = "Green grapes",
                itemNumber = "725545",
                imagePath = R.drawable.greengrapes.toString()
            ),
            Item(
                itemName = "Navel oranges",
                itemNumber = "980167341",
                imagePath = R.drawable.redgrapes.toString()
            ),
            Item(
                itemName = "Organic bananas",
                itemNumber = "105832",
                imagePath = R.drawable.organicbananas.toString()
            ),
            Item(
                itemName = "Organic Gala Apples",
                itemNumber = "980319308",
                imagePath = R.drawable.organicgalaapples.toString()
            ),
            Item(
                itemName = "Pineapple",
                itemNumber = "835891",
                imagePath = R.drawable.pineapple.toString()
            ),
            Item(
                itemName = "Raspberries",
                itemNumber = "33249",
                imagePath = R.drawable.raspberries.toString()
            ),
            Item(
                itemName = "Red grapes",
                itemNumber = "72553",
                imagePath = R.drawable.redgrapes.toString()
            ),
            Item(
                itemName = "Strawberries",
                itemNumber = "749972",
                imagePath = R.drawable.strawberries.toString()
            ),
            Item(
                itemName = "Watermelon",
                itemNumber = "825216",
                imagePath = R.drawable.watermelon.toString()
            ),
            Item(
                itemName = "Farmer John Bacon ",
                itemNumber = "980154155",
                imagePath = R.drawable.bacon.toString()
            ),
            Item(
                itemName = "Babybel Original",
                itemNumber = "980156320",
                imagePath = R.drawable.babybellight.toString()
            ),
            Item(
                itemName = "Babybel Light",
                itemNumber = "980157831",
                imagePath = R.drawable.original.toString()
            ),
            Item(
                itemName = "Ice (20 lbs)",
                itemNumber = "693656",
                imagePath = R.drawable.ice.toString()
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
