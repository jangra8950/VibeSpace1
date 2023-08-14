package com.app.vibespace.models.profile

data class UserListModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val totalCount: Int,
        val users: List<User>
    ){
        data class User(
            val authType: String,
            val avatar: String,
            val bio: String,
            val email: String,
            val firstName: String,
            val fullName: String,
            val gender: String,
            val height: Double,
            val insertDate: Int,
            val isEditable: Boolean,
            val isPrivate: Boolean,
            val lastName: String,
            val location: Location,
            val occupation: String,
            val profilePic: String,
            val status: String,
            val streak: Int,
            val totalFollower: Int,
            val totalFollowing: Int,
            val universityId: String,
            val universityName: String,
            val userId: String,
            val vibes: Int
        ){
            data class Location(
                val lat: Double,
                val lng: Double
            )
        }
    }
}