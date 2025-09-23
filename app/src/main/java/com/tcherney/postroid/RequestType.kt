package com.tcherney.postroid

enum class RequestType(val value: String) {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete")
}