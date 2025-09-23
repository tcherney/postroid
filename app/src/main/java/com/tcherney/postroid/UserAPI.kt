package com.tcherney.postroid

class UserAPI {
    val endPoint: String = ""
    val headers: List<Pair<String,String>> = listOf()
    val params: List<Pair<String,String>> = listOf()
    val bodyContent: String = ""
    var requestType: RequestType = RequestType.GET
    fun execute() {
        //TODO use okhttp to make request
    }
}