package com.tcherney.postroid

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserAPI::class, UserAPICollection::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userAPIDao(): UserAPIDao
    abstract fun userAPICollectionDao(): UserAPICollectionDao
}