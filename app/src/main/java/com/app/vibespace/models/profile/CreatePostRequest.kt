package com.app.vibespace.models.profile

data class CreatePostRequest(
    var caption: String?=null,
    var location: Location?=null,
    var postVisibility: String?=null
){
    data class Location(
        var lat: Int,
        var lng:Int
    )
}