package com.app.vibespace.models.setting

data class FollowersModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val followers: List<Follower>,
        val totalCount: Int
    ){
        data class Follower(
            val connectId: String,
            val insertDate: Int,
            val isPostNotification: Boolean,
            val userDetails: UserDetails
        ){
            data class UserDetails(
                val avatar: String,
                val bio: String,
                val email: String,
                val firstName: String,
                val lastName: String,
                val profilePic: String,
                val status: String,
                val totalFollower: Int,
                val totalFollowing: Int,
                val userId: String
            )
        }
    }
}