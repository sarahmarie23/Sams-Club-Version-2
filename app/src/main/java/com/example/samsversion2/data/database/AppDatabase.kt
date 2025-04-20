package com.example.samsversion2.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.samsversion2.R
import com.example.samsversion2.data.model.Item
import com.example.samsversion2.data.model.ListEntity
import com.example.samsversion2.data.model.ListItemCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Item::class, ListEntity::class, ListItemCrossRef::class], version = 10)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {

        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {

            return instance ?: synchronized(LOCK) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inventory_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d("AppDatabase", "onCreate:")
                            super.onCreate(db)

                        }
                    })
                    .build().also { instance = it }
            }
        }
    }
}

