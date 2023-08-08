package com.app.vibespace.models.profile

data class BlockUserModel(
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
            val blockId: String,
            val otherUserId: String,
            val userId: String
        )
    }
}