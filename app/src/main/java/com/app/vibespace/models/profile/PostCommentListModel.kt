package com.app.vibespace.models.profile

data class PostCommentListModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val comments: List<Comment>
    ){
        data class Comment(
            val commentId: String,
            val insertDate: Int,
            val isLiked: Boolean,
            val postId: String,
            val replyData: List<Any>?,
            val text: String,
            val userDetails: UserDetails,
            val userId: String
        ){
            data class UserDetails(
                val avatar: String,
                val firstName: String,
                val profilePic: String
            )
        }
    }
}