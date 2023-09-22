package com.app.vibespace.models.setting

data class EditProfileModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val authType: String,
        val avatar: String,
        val bio: String,
        val dob: String,
        val email: String,
        val firstName: String,
        val insertDate: Int,
        val isPrivate: Boolean,
        val lastName: String,
        val location: Location,
        val mascotIcon: String,
        val occupation: String,
        val profilePic: String,
        val status: String,
        val streak: Int,
        val uniShortName: String,
        val universityId: String,
        val universityName: String,
        val userId: String
    ){
        data class Location(
            val lat: Double,
            val lng: Double
        )
    }
}