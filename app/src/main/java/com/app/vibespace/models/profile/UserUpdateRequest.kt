package com.app.vibespace.models.profile

data class UserUpdateRequest(
    var gender: String?=null,
    var height: Double?=null,
    var location: Location?=null,
    var occupation: String?=null
){
    data class Location(
        var lat: Double,
        var lng: Double
    )
}