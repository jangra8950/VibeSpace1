package com.app.vibespace.models.registration

data class GetPeopleModel(
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val userList: List<User>
    ){
        data class User(
            val avatar: String,
            val bio: String,
            val dob: String,
            val email: String,
            val firstName: String,
            val gender: String,
            val insertDate: Int,
            val isPrivate: Boolean,
            val lastName: String,
            val location: Location,
            val mascotIcon: String,
            val occupation: String,
            val profilePic: String,
            val status: String,
            val unreadPostCount: Int,
            val userId: String
        ){
            data class Location(
                val lat: Double,
                val lng: Double
            )
        }
    }
}