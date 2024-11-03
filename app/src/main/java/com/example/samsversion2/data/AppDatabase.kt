package com.example.samsversion2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.samsversion2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Item::class], version = 2)
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
                    "item_table"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate the database here
                            populateDatabase(getInstance(context).itemDao())
                        }
                    })
                    .build().also { instance = it }
            }
        }

        private fun populateDatabase(itemDao: ItemDao) {
            // Add sample items to the database
            CoroutineScope(Dispatchers.IO).launch {
                val items = listOf(
                    Item(itemName = "Avocados", itemNumber = "622943", imageUrl = R.drawable.avocados),
                    Item(itemName = "Bananas", itemNumber = "362153", imageUrl = R.drawable.bananas),
                    Item(itemName = "Blackberries", itemNumber = "284479", imageUrl = R.drawable.blackberries),
                    Item(itemName = "Blueberries", itemNumber = "279457 ", imageUrl = R.drawable.blueberries),
                    Item(itemName = "Cantaloupe", itemNumber = "867552", imageUrl = R.drawable.cantaloupe),
                    Item(itemName = "Cara cara oranges", itemNumber = "967219", imageUrl = R.drawable.caracaraoranges),
                    Item(itemName = "Clementine mandarins", itemNumber = "457334", imageUrl = R.drawable.clementines),
                    Item(itemName = "Green grapes", itemNumber = "725545", imageUrl = R.drawable.greengrapes),
                    Item(itemName = "Navel oranges", itemNumber = "980167341", imageUrl = R.drawable.redgrapes),
                    Item(itemName = "Organic bananas", itemNumber = "105832", imageUrl = R.drawable.organicbananas),
                    Item(itemName = "Organic Gala Apples", itemNumber = "980319308", imageUrl = R.drawable.organicgalaapples),
                    Item(itemName = "Pineapple", itemNumber = "835891", imageUrl = R.drawable.pineapple),
                    Item(itemName = "Raspberries", itemNumber = "33249", imageUrl = R.drawable.raspberries),
                    Item(itemName = "Red grapes", itemNumber = "72553", imageUrl = R.drawable.redgrapes),
                    Item(itemName = "Strawberries", itemNumber = "749972", imageUrl = R.drawable.strawberries),
                    Item(itemName = "Watermelon", itemNumber = "825216", imageUrl = R.drawable.watermelon),
                    Item(itemName = "Farmer John Bacon ", itemNumber = "980154155", imageUrl = R.drawable.bacon),
                    Item(itemName = "Babybel Original", itemNumber = "980156320", imageUrl = R.drawable.babybellight),
                    Item(itemName = "Babybel Light", itemNumber = "980157831", imageUrl = R.drawable.original),
                    Item(itemName = "Ice (20 lbs)", itemNumber = "693656", imageUrl = R.drawable.ice)
                )
                itemDao.insertAll(items)
            }
        }
    }
}

