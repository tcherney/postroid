package com.tcherney.postroid

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import okio.IOException

class RequestWrapper {
    val client: OkHttpClient = OkHttpClient()
    fun get(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                println(response.body.string())
            }
        })
    }
    fun post(formBody: FormBody, url: String) {
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                println(response.body.string())
            }
        })
    }
}

