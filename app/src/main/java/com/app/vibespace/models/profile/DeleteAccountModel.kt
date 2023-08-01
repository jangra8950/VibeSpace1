package com.app.vibespace.models.profile

data class DeleteAccountModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val message: String
    )
}