package com.tcherney.postroid

import android.util.Log
import androidx.collection.SimpleArrayMap
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

private val client = OkHttpClient()
@Entity(tableName = "user_api",
        foreignKeys = [
            ForeignKey(
                entity = UserAPICollectionInternal::class,
                parentColumns = arrayOf("collection_id"),
                childColumns = arrayOf("collection_id"),
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE,
            )
        ])
data class UserAPI(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "api_id") var apiID: Long = 0,
    @ColumnInfo(name = "collection_id") var collectionID: Long = 0,
    @ColumnInfo(name = "endpoint") var endPoint: String = "",
    @ColumnInfo(name = "headers") val headers: HashMap<String,String> = hashMapOf(),
    @ColumnInfo(name = "params") val params: HashMap<String,String> = hashMapOf(),
    @ColumnInfo(name = "body_content") val bodyContent: String = "",
    @ColumnInfo(name = "request_type") var requestType: RequestType = RequestType.GET){
    //TODO sessions, loading spinner
    fun execute(onCompletion: (Response) -> Unit) {
        val headersBuilder = Headers.Builder()
        for (h in headers.entries) {
            headersBuilder.add(h.key,h.value)
        }
        if (requestType == RequestType.GET) {
            val httpUrl = endPoint.toHttpUrlOrNull()?.newBuilder()
            for (p in params.entries) {
                httpUrl?.addQueryParameter(p.key, p.value)
            }
            val request = Request.Builder()
                .url(httpUrl!!.build())
                .headers(headersBuilder.build())
                .build()
            Log.d("UserAPI", "Sending request $request")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        Log.d("UserAPI", "Received response $response")
                        onCompletion(response)
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    }
                }
            })
        }
        else if (requestType == RequestType.POST) {
            val request = Request.Builder()
                .url(endPoint)
                .post(bodyContent.toRequestBody()) //TODO different post types
                .headers(headersBuilder.build())
                .build()
            Log.d("UserAPI", "Sending request $request")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        Log.d("UserAPI", "Received response $response")
                        onCompletion(response)
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    }
                }
            })
        }
    }
}

@Dao
interface UserAPIDao {
    @Query("SELECT * FROM user_api")
    fun getAll(): List<UserAPI>

    @Query("SELECT * FROM user_api WHERE api_id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UserAPI>

    @Insert
    suspend fun insertAll(vararg userAPIs: UserAPI)

    @Upsert
    suspend fun upsertAPI(userAPI: UserAPI)

    @Update
    suspend fun updateUserAPI(vararg userAPIs: UserAPI)

    @Delete
    suspend fun delete(userAPI: UserAPI)
}