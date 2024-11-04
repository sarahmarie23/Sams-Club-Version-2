package com.example.samsversion2.di

import android.app.Application
import androidx.room.Room
import com.example.samsversion2.data.database.AppDatabase
import com.example.samsversion2.data.repository.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "inventory_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideItemRepository(database: AppDatabase): ItemRepository {
        return ItemRepository(database.itemDao())
    }
}
