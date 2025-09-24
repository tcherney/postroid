package com.tcherney.postroid

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

private val client = OkHttpClient()
class UserAPI {
    val endPoint: String = ""
    val headers: List<Pair<String,String>> = listOf()
    val params: List<Pair<String,String>> = listOf()
    val bodyContent: String = ""
    var requestType: RequestType = RequestType.GET
    fun execute() {
        val headersBuilder = Headers.Builder()
        for (h in headers) {
            headersBuilder.add(h.first,h.second)
        }
        //TODO add mutable to let ui know request is done
        if (requestType == RequestType.GET) {
            val request = Request.Builder()
                .url(endPoint)
                .headers(headersBuilder.build())
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        for ((name, value) in response.headers) {
                            println("$name: $value")
                        }

                        println(response.body.string())
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
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        for ((name, value) in response.headers) {
                            println("$name: $value")
                        }

                        println(response.body.string())
                    }
                }
            })
        }
    }
}