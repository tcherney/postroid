package com.tcherney.postroid

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "user_api_collection")
class UserAPICollection(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "collection_name") val collectionName: String = "Untitled",
    @ColumnInfo(name = "user_apis") val userAPIs: ArrayList<UserAPI> = arrayListOf(UserAPI())){
}

@Dao
interface UserAPICollectionDao {
    @Query("SELECT * FROM user_api_collection")
    fun getAll(): List<UserAPICollection>

    @Query("SELECT * FROM user_api_collection WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UserAPICollection>

    @Insert
    fun insertAll(vararg userAPICollections: UserAPICollection)

    @Delete
    fun delete(userAPICollection: UserAPICollection)
}