package com.example.samsversion2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
data class ListEntity(
    @PrimaryKey(autoGenerate = true) val listId: Long = 0,
    val listName: String
)
