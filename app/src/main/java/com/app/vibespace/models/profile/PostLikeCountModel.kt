package com.app.vibespace.models.profile

data class PostLikeCountModel(
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
            val insertDate: Int,
            val likeId: String,
            val postId: String,
            val userId: String,
            val userName: String
        )
    }
}