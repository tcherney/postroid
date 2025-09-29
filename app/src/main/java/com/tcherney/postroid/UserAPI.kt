package com.tcherney.postroid

import androidx.collection.SimpleArrayMap
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
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
@Entity(tableName = "user_api")
class UserAPI(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "endpoint") val endPoint: String = "",
    //TODO we need retofit to serialize this as json before storing
    @ColumnInfo(name = "headers") val headers: HashMap<String,String> = hashMapOf(),
    @ColumnInfo(name = "params") val params: HashMap<String,String> = hashMapOf(),
    @ColumnInfo(name = "body_content") val bodyContent: String = "",
    @ColumnInfo(name = "request_type") var requestType: RequestType = RequestType.GET){
    fun execute(onCompletion: (Response) -> Unit) {
        val headersBuilder = Headers.Builder()
        for (h in headers.entries) {
            headersBuilder.add(h.key,h.value)
        }
        //TODO add mutable to let ui know request is done
        if (requestType == RequestType.GET) {
            val httpUrl = endPoint.toHttpUrlOrNull()?.newBuilder()
            for (p in params.entries) {
                httpUrl?.addQueryParameter(p.key, p.value)
            }
            val request = Request.Builder()
                .url(httpUrl!!.build())
                .headers(headersBuilder.build())
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
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

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
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

    @Query("SELECT * FROM user_api WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UserAPI>

    @Insert
    fun insertAll(vararg userAPIs: UserAPI)

    @Delete
    fun delete(userAPI: UserAPI)
}