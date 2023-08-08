package com.app.vibespace.models.profile

data class PostDeleteModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val message: String
    )
}
