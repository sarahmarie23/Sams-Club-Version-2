package com.example.samsversion2.data.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["listId", "itemId"],
    indices = [Index(value = ["itemId"]), Index(value = ["listId"])],
    tableName = "list_item_cross_ref"
    )

data class ListItemCrossRef(
    val listId: Long = 0,
    val itemId: Long
)

