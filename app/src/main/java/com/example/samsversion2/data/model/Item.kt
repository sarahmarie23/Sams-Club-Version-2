package com.example.samsversion2.data.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true) val itemId: Long = 0,
    val itemName: String,
    val itemNumber: String,
    @DrawableRes val imageUrl: Int,
    val inStock: Boolean = true
)

