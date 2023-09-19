package com.app.vibespace.models.profile

data class PendingRequestModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val followRequests: List<Any>,
        val totalCount: Int
    )
}