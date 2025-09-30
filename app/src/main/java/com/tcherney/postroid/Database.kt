package com.tcherney.postroid

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringMap(value: HashMap<String,String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringMap(value: String?): HashMap<String,String>? {
        val mapType = object : TypeToken<HashMap<String, String>>() {}.type
        return Gson().fromJson(value, mapType)
    }
}
@Database(entities = [UserAPI::class, UserAPICollectionInternal::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userAPIDao(): UserAPIDao
    abstract fun userAPICollectionDao(): UserAPICollectionDao
}