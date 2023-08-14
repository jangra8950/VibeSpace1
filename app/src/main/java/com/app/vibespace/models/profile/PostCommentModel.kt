package com.app.vibespace.models.profile

data class PostCommentModel(
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
            val commentId: String,
            val insertDate: Int,
            val postId: String,
            val text: String,
            val userId: String,
            val userProfilePic: String
        )
    }
}