package com.app.vibespace.models.profile

data class FollowModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val message: String,
        val response: Response
    ){
        data class Response(
            val conncetId: String,
            val totalFollower: Int,
            val totalFollowing: Int
        )
    }
}