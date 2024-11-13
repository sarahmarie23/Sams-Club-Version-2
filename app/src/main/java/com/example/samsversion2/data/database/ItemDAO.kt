package com.example.samsversion2.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.example.samsversion2.data.model.Item
import com.example.samsversion2.data.model.ListEntity
import com.example.samsversion2.data.model.ListItemCrossRef

@RewriteQueriesToDropUnusedColumns
@Dao
interface ItemDao {
    @Transaction
    @Query("""
        SELECT * FROM item_table
        INNER JOIN list_item_cross_ref ON item_table.itemId = list_item_cross_ref.itemId
        WHERE list_item_cross_ref.listId = :listId
    """)
    fun getItemsForList(listId: Long): LiveData<List<Item>>

    @Query("SELECT * FROM item_table ORDER BY itemName ASC")
    fun getAllItems(): LiveData<List<Item>>

    @Query("SELECT listId FROM list_table WHERE listName = :name LIMIT 1")
    suspend fun getListIdByName(name: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(listEntity: ListEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListItemCrossRef(crossRef: ListItemCrossRef)

    @Query("SELECT COUNT(*) FROM item_table")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM list_item_cross_ref")
    suspend fun getCountRef(): Int

    @Query("SELECT COUNT(*) FROM list_item_cross_ref WHERE listId = :listId")
    suspend fun getItemCountForList(listId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemsFromList(items: List<Item>): List<Long>

    @Query("DELETE FROM item_table WHERE itemId = :itemId")
    suspend fun deleteItem(itemId: Long)

    @Query("DELETE FROM list_item_cross_ref WHERE itemId = :itemId")
    suspend fun deleteCrossRefWithItemId(itemId: Long)

    @Query("DELETE FROM list_item_cross_ref WHERE listId = :listId AND itemId = :itemId")
    suspend fun deleteCrossRef(listId: Long, itemId: Long)
}

data class ListWithItems(
    @Embedded val list: ListEntity,
    @Relation(
        parentColumn = "listId",
        entityColumn = "itemId",
        associateBy = Junction(ListItemCrossRef::class)
    )
    val items: List<Item>
)