package com.app.vibespace.models.profile


data class UserUpdateModel(
    var apiId: String,
    var data: Data,
    var message: String,
    var statusCode: Int
){
    data class Data(
        var accessToken: String,
        var deviceToken: String,
        var deviceType: String,
        var authType: String,
        var bio: String,
        var dob: String,
        var email: String,
        var firstName: String,
        var insertDate: Int,
        var isPrivate: Boolean,
        var lastName: String,
        var location: Location?=null,
        var occupation: String,
        var profilePic: String,
        var status: String,
        var totalFollowing: Int,
        var totalFollower: Int,
        var universityId: String,
        var universityName: String,
        var userId: String,
        var gender: String,
        var height: Double,
    ){
        data class Location(
            var lat: Double,
            var lng: Double
        )
    }
}