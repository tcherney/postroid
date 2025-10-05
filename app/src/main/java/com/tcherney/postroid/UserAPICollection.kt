package com.tcherney.postroid

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

//
//data class UserAPICollection(
//    @Embedded val internalCollection: UserAPICollectionInternal? = null,
//    @Relation(
//        parentColumn = "collectionID",
//        entityColumn = "apiID",
//        associateBy = Junction(UserAPICollectionCrossRef::class)
//    )
//    val userAPIs: ArrayList<UserAPI> = arrayListOf(UserAPI())){
//}

data class UserAPICollection(
    @Embedded val internalCollection: UserAPICollectionInternal? = null,
    @Relation(
        entity = UserAPI::class,
        parentColumn = "collection_id",
        entityColumn = "collection_id",
    )
    val userAPIs: List<UserAPI> = listOf(UserAPI())){
}

@Entity(tableName = "user_api_collection")
data class UserAPICollectionInternal(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "collection_id") var collectionID: Long = 0,
    @ColumnInfo(name = "collection_name") var collectionName: String = "Untitled",
)

@Entity(primaryKeys = ["collection_id, api_id"])
data class UserAPICollectionCrossRef(
    val collectionID: Long,
    val apiID: Long,
)

@Dao
interface UserAPICollectionDao {

    @Transaction
    @Query("SELECT * FROM user_api_collection")
    fun getAll(): Flow<List<UserAPICollection>>

    @Transaction
    @Query("SELECT * FROM user_api_collection WHERE collection_id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): Flow<List<UserAPICollection>>

    @Insert
    suspend fun insertAll(vararg userAPICollections: UserAPICollectionInternal)

    @Insert
    suspend fun insertParent(userAPICollection: UserAPICollectionInternal)

    @Insert
    suspend fun insertChild(userAPI: UserAPI)

    @Transaction
    suspend fun insertParentAndChild(parent: UserAPICollectionInternal, child: UserAPI) {
        insertParent(parent)
        child.collectionID = parent.collectionID
        Log.d("UserAPICollection", "Where this " +child.collectionID.toString() + " " + parent.collectionID.toString())
        insertChild(child)
    }

    @Upsert
    suspend fun upsertAPI(userAPICollection: UserAPICollectionInternal)

    @Delete
    suspend fun delete(userAPICollection: UserAPICollectionInternal)

    @Update
    suspend fun updateUserAPI(vararg userAPICollections: UserAPICollectionInternal)
}