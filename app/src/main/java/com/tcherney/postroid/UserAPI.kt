package com.tcherney.postroid

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
class UserAPI(val endPoint: String = "",
    val headers: ArrayList<Pair<String,String>> = arrayListOf(),
    val params: ArrayList<Pair<String,String>> = arrayListOf(),
    val bodyContent: String = "",
    var requestType: RequestType = RequestType.GET){
    fun execute(onCompletion: (Response) -> Unit) {
        val headersBuilder = Headers.Builder()
        for (h in headers) {
            headersBuilder.add(h.first,h.second)
        }
        //TODO add mutable to let ui know request is done
        if (requestType == RequestType.GET) {
            val httpUrl = endPoint.toHttpUrlOrNull()?.newBuilder()
            for (p in params) {
                httpUrl?.addQueryParameter(p.first, p.second)
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