package com.app.vibespace.models.profile

data class CreatePostModel(
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
            val accessType: Int,
            val caption: String,
            val commentCount: Int,
            val insertDate: Int,
            val isLiked: Boolean,
            val likeCount: Int,
            val location: Location,
            val postId: String,
            val postVisibility: String,
            val userDetails: UserDetails,
            val userId: String
        ){
            data class Location(
                val lat: Double,
                val lng: Double
            )
            data class UserDetails(
                val email: String,
                val firstName: String,
                val lastName: String,
                val profilePic: String
            )
        }
    }
}