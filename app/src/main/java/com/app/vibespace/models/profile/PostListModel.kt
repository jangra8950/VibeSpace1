package com.app.vibespace.models.profile

data class PostListModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val posts: List<Post>,
        val totalCount: Int
    ){
        data class Post(
            val accessType: Int,
            val avatarImage: String,
            val caption: String,
            val commentCount: Int,
            val completeName: String,
            val insertDate: Int,
            val isFollow: Boolean,
            val isLiked: Boolean,
            val isOwnPost: Boolean,
            val likeCount: Int,
            val location: Location,
            val postId: String,
            val postVisibility: String,
            val reportCount: Int,
            val userDetails: UserDetails,
            val userId: String
        ){
            data class UserDetails(
                val email: String,
                val firstName: String,
                val lastName: String,
                val mascotIcon: String,
                val profilePic: String
            )

            data class Location(
                val lat: Double,
                val lng: Double
            )
        }
    }
}